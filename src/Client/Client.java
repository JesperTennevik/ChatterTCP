package Client;

import Client.UIComponents.InputField;
import Client.UIComponents.MessageArea;
import Client.UIComponents.UsersArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends JFrame {
    String ip;
    int port;
    String name;

    JPanel panel;
    JTextArea usersArea;
    JTextField inputField;
    JScrollPane messageArea;

    Socket socket;
    PrintWriter out;
    BufferedReader in;

    ArrayList<String> connectedUsers = new ArrayList<>();

    public Client(){
        boolean validInput = false;
        while(!validInput) {
            String chatRoom = JOptionPane.showInputDialog(null, "Ip:Port");
            if (chatRoom == null) { return; }

            try { validateChatRoomInput(chatRoom); }
            catch(InvalidInputIpException e){
                JOptionPane.showMessageDialog(null, e.getMessage());
                continue;
            }

            String[] split = chatRoom.split(":");
            ip = split[0];
            port = Integer.parseInt(split[1]);

            try{
                socket = new Socket("127.0.0.1", port);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                validInput = true;
            }
            catch (IOException e) { JOptionPane.showMessageDialog(null, "Unable to connect."); }
        }

        while(name == null){
            name = JOptionPane.showInputDialog(null, "Name: ");
            if(name == null){
                JOptionPane.showMessageDialog(null, "Invalid name.");
            }
        }

        panel = new JPanel(new BorderLayout());
        usersArea = new UsersArea();
        messageArea = new MessageArea();
        inputField = new InputField(name);

        setTitle("Chatter");
        add(panel);
        panel.add(messageArea, BorderLayout.CENTER);
        panel.add(usersArea, BorderLayout.EAST);
        panel.add(inputField, BorderLayout.SOUTH);

        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Thread.startVirtualThread(() -> {
            try {
                String message;
                while((message = in.readLine()) != null) {
                    if (message.startsWith("UserJoined:")) {
                        String name = message.split(":")[1];
                        connectedUsers.add(name);
                        updateUsers();
                    } else if (message.startsWith("UserLeft:")) {
                        String name = message.split(":")[1];
                        connectedUsers.remove(name);
                        updateUsers();
                    } else if(message.startsWith("ConnectedUsers:")){
                        message = message.substring("ConnectedUsers:".length());
                        String[] users = message.split(",");
                        for(String name : users){
                            connectedUsers.add(name);
                        }
                        updateUsers();
                    }

                    else {
                        final String msg = message;
                        SwingUtilities.invokeLater(() -> ((JTextArea) messageArea.getViewport().getComponent(0))
                                .append(msg + "\n"));
                    }
                }
            } catch (IOException e) {
                IO.println("Disconnected.");
            }
        });

        inputField.addActionListener(e -> {
            String text = inputField.getText();
            out.println(name + ": " + text);
            inputField.setText("");
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                out.println("UserLeft:"+name);
                try { socket.close(); }
                catch (IOException ex) { ex.printStackTrace(); }
            }
        });

        out.println("UserJoined:"+name);
    }

    private void updateUsers(){
        SwingUtilities.invokeLater(() -> {
            usersArea.setText("");
            for(String user : connectedUsers){
                usersArea.append(user+"\n");
            }
        });
    }

    private void validateChatRoomInput(String chatRoom) throws InvalidInputIpException {
        if(!chatRoom.contains(":")) { throw new InvalidInputIpException("Requires format: Ip:Port"); }

        String[] split = chatRoom.split(":");
        if (split.length != 2) { throw new InvalidInputIpException("Requires format: Ip:Port."); }
        else if (split[0].length() >= 15) { throw new InvalidInputIpException("Invalid ip address."); }
        else if (split[1].length() != 5) { throw new InvalidInputIpException("Invalid port."); }

        try { Integer.parseInt(split[1]); }
        catch (Exception e) { throw new InvalidInputIpException("Invalid Port"); }

        // Refactor ip check. split on "." convert to int and check interval value.
        // can/should add validations for ip och port ranges.
    }

    static void main(String[] args){ SwingUtilities.invokeLater(Client::new); }
}

class InvalidInputIpException extends Exception{
    public InvalidInputIpException(String m){
        super(m);
    }
}