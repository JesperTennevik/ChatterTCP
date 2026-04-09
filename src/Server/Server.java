package Server;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {
    static ArrayList<ClientData> clients = new ArrayList<>();

    public Server(Socket socket){
        Thread.startVirtualThread(() -> {
            try (
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ){
                String message;
                try {
                    while((message = in.readLine()) != null) {
                        if(message.startsWith("UserJoined:")){
                            String name = message.split(":")[1];
                            if(!clients.isEmpty()){
                                String connectedUsers = "";
                                for(ClientData client : clients){
                                    connectedUsers += client.name + ",";
                                }
                                out.println("ConnectedUsers:"+connectedUsers);
                            }
                            clients.add(new ClientData(name, out));
                            broadcast("UserJoined:"+name);
                        }
                        else if(message.startsWith("UserLeft:")){
                            String name = message.split(":")[1];
                            for(int i = 0; i < clients.size(); i++){
                                if(clients.get(i).name.equals(name)){
                                    clients.remove(i);
                                    break;
                                }
                            }
                            broadcast("UserLeft:"+name);
                        }else{
                            broadcast(message);
                        }
                    }
                } catch(IOException e){ e.printStackTrace(); }
            }
            catch(SocketException e){
                e.printStackTrace();
            }
            catch (IOException e) { e.printStackTrace(); }
        });
    }

    synchronized void broadcast(String message){
        for(ClientData client : clients){
            client.out.println(message);
        }
    }

}

class ClientData {
    String name;
    PrintWriter out;

    public ClientData(String name, PrintWriter out) {
        this.out = out;
        this.name = name;
    }
}

