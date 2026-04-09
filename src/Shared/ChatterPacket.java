package Shared;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatterPacket implements Serializable {

    public enum PacketType {
        UserJoined,
        UserLeft,
        ConnectedUsers,
        TextMessage
    }

    String sender;
    PacketType type;
    String msg;
    ArrayList<String> connectedUsers;

    public ChatterPacket(String sender, PacketType msgType){
        this.sender = sender;
        this.type = msgType;
    }

    public ChatterPacket(String sender, PacketType msgType, String msg){
        this.sender = sender;
        this.type = msgType;
        this.msg = msg;
    }

    public ChatterPacket(String sender, PacketType msgType, ArrayList<String> connectedUsers){
        this.sender = sender;
        this.type = msgType;
        this.connectedUsers = connectedUsers;
    }

    public PacketType getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getMsg(){
        return msg;
    }

    public ArrayList<String> getConnectedUsers(){
        return connectedUsers;
    }
}
