package org.example;

import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

public class ConnectFourServer extends WebSocketServer {
    public static Gson gson = new Gson();
    public static LinkedList<User> connectedUsers = new LinkedList<>();
    public static InputLayer input;

    public static void main(String[] args){
        int port = 81;

        WebSocketServer server = new ConnectFourServer(new InetSocketAddress(port));
        server.run();
    }

    public ConnectFourServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("New Connection To " + webSocket.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println("Closed " + webSocket.getRemoteSocketAddress() + " with exit code " + i + " extra info: " + s);
        int userIndex = -1;
        for (int k = 0; k < connectedUsers.size(); k++){
            if (connectedUsers.get(k).address == webSocket.getRemoteSocketAddress()){
                userIndex = k;
                break;
            }
        }
        if (userIndex != -1){
            connectedUsers.remove(userIndex);
        }
        broadcast(gson.toJson(new Comm("update", connectedUsers)));
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println("(" + webSocket.getRemoteSocketAddress() + ")Received Message: " + s);
        Comm comm = gson.fromJson(s, Comm.class);
        switch (comm.type) {
            case "login" -> {
                connectedUsers.add(new User(comm.username, webSocket.getRemoteSocketAddress()));
                broadcast(gson.toJson(new Comm("update", connectedUsers)));
            }
            case "message" -> {
                broadcast(s);
            }
            case "input"-> {
                if (input != null){
                    if (comm.player != input.getTurn()) return;
                    if (input.checkMove(comm.column)){
                        input.play(comm.column);
                        broadcast(gson.toJson(new Comm("erorr", "Not Implemented!")));
                    } else {
                        webSocket.send(gson.toJson(new Comm("error", "Column Is Full!")));
                    }
                } else {
                    webSocket.send(gson.toJson(new Comm("error", "Game Not Started!")));
                }
            }
            case "newgame" -> {
                input = new InputLayer(comm.width, comm.height, comm.players.toCharArray());
            }
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        System.out.println("ERROR! " + webSocket.getRemoteSocketAddress() + ":" + e);
    }

    @Override
    public void onStart() {
        System.out.println("Server Started!");
    }
}