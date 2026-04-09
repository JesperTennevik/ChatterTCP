package Server;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {
    static int port;

    static void main(String[] args){

        boolean validInput = false;
        while(!validInput){
            String portStr = JOptionPane.showInputDialog(null, "Port:");
            if(portStr == null){ return; }

            if(isValidPort(portStr)) { validInput = true; }
            else{ JOptionPane.showMessageDialog(null, "Invalid port"); }
        }

        try(ServerSocket serverSocket = new ServerSocket(port)){
            while(true){
               Socket socket = serverSocket.accept();
               new Server(socket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static private boolean isValidPort(String portStr) {
        try {
            port = Integer.parseInt(portStr);
            return port >= 1 && port <= 65535;
        } catch (NumberFormatException e) { return false; }
    }
}
