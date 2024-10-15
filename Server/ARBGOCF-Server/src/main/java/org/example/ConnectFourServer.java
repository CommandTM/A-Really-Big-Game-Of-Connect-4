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

    public static void main(String[] args){
        String host = "localhost";
        int port = 81;

        WebSocketServer server = new ConnectFourServer(new InetSocketAddress(host, port));
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