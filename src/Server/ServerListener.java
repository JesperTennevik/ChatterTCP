package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {
    static void main(String[] args){
        try(ServerSocket serverSocket = new ServerSocket(12345)){
            while(true){
               Socket socket = serverSocket.accept();
               new Server(socket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
