package Server;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import Shared.ChatterPacket;

public class Server {
    static CopyOnWriteArrayList<ClientData> clients = new CopyOnWriteArrayList<>();

    public Server(Socket socket){
        Thread.startVirtualThread(() -> {
            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ){
                ChatterPacket packet;
                try {
                    while((packet = (ChatterPacket) in.readObject()) != null) {
                        switch(packet.getType()){
                            case ChatterPacket.PacketType.UserJoined -> {
                                if(!clients.isEmpty()){
                                    ArrayList<String> connectedUsers = new ArrayList<>();
                                    for(ClientData client : clients){
                                        connectedUsers.add(client.name);
                                    }
                                    sendPacket(out, new ChatterPacket(packet.getSender(),
                                            ChatterPacket.PacketType.ConnectedUsers, connectedUsers));
                                }
                                clients.add(new ClientData(packet.getSender(), out));
                                broadcast(new ChatterPacket(packet.getSender(), ChatterPacket.PacketType.UserJoined));
                            }
                            case ChatterPacket.PacketType.UserLeft -> {
                                for(int i = 0; i < clients.size(); i++){
                                    if(clients.get(i).name.equals(packet.getSender())){
                                        clients.remove(i);
                                        break;
                                    }
                                }
                                broadcast(new ChatterPacket(packet.getSender(), ChatterPacket.PacketType.UserLeft));
                            }
                            case ChatterPacket.PacketType.TextMessage -> broadcast(packet);
                            default -> IO.println("Unknown packet type.");
                        }
                    }
                }
                catch(IOException e){ e.printStackTrace(); }
                catch (ClassNotFoundException e) { IO.println("Error receiving packet."); }
            }
            catch(SocketException e){
                e.printStackTrace();
            }
            catch (IOException e) { e.printStackTrace(); }
        });
    }

    void broadcast(ChatterPacket packet){
        for(ClientData client : clients){
            sendPacket(client.out, packet);
        }
    }

    private void sendPacket(ObjectOutputStream out, ChatterPacket packet){
        try {
            out.writeObject(packet);
            out.flush();
        } catch (IOException e) { JOptionPane.showMessageDialog(null, "Error sending packet."); }
    }
}

class ClientData {
    String name;
    ObjectOutputStream out;

    public ClientData(String name, ObjectOutputStream out) {
        this.out = out;
        this.name = name;
    }
}

