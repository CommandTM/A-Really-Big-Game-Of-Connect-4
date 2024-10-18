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

    /**
     * Initialize server
     * @param args Not used
     */
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
        // When a user disconnects, removed them from connect users
        int userIndex = -1;
        for (int k = 0; k < connectedUsers.size(); k++){
            if (connectedUsers.get(k).address == webSocket.getRemoteSocketAddress()){
                userIndex = k;
                break;
            }
        }
        if (userIndex != -1) {
            connectedUsers.remove(userIndex);
        }
        if (!connectedUsers.isEmpty()){
            // Update everyone's player list
            broadcast(gson.toJson(new Comm("update", connectedUsers)));
        } else {
            // End the current game if all players have left
            input = null;
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println("(" + webSocket.getRemoteSocketAddress() + ")Received Message: " + s);
        Comm comm = gson.fromJson(s, Comm.class);
        // Whenever we receive a packet, parse it's type and handle depending on that
        switch (comm.type) {
            case "login" -> {
                // Handshake with the user who sent a login packet

                // Add them to connect users
                connectedUsers.add(new User(comm.username, webSocket.getRemoteSocketAddress()));
                // Send them information on the current game
                if (input != null){
                    String players = "";
                    for (char player : input.players){
                        players += player;
                    }
                    webSocket.send(gson.toJson(new Comm("login", true, input.board.width, input.board.height, input.board.board, players, input.turn)));
                } else {
                    webSocket.send(gson.toJson(new Comm("login", false)));
                }
                // Send them the user list
                broadcast(gson.toJson(new Comm("update", connectedUsers)));
            }
            // Should never be sent by clients
            case "message" -> {
                // Echo back any message packets received
                broadcast(s);
            }
            case "input"-> {
                // Make sure there is a board to play on
                if (input != null){
                    comm.column -= 1;
                    // Check is move is valid
                    if (input.checkMove(comm.column)){
                        // Play move and send updated board
                        broadcast(gson.toJson(new Comm("message", "Server", (comm.username + " Has Played " + input.getTurn() + " On Column " + (comm.column+1)))));
                        if (!input.play(comm.column)){
                            String players = "";
                            for (char player : input.players){
                                players += player;
                            }
                            broadcast(gson.toJson(new Comm("login", true, input.board.width, input.board.height, input.board.board, players, input.turn)));
                        } else {
                            // If someone wins, don't send the board, instead send a victory message and tell client the game is over
                            broadcast(gson.toJson(new Comm("message", "Server", (input.getPreviousTurn() + " Has Won!"))));
                            input = null;
                            broadcast(gson.toJson(new Comm("login", false)));
                        }
                    } else {
                        webSocket.send(gson.toJson(new Comm("error", "Column Is Full!")));
                    }
                } else {
                    webSocket.send(gson.toJson(new Comm("error", "Game Not Started!")));
                }
            }
            case "newgame" -> {
                // Start a new game with defined parameters, send out the game info after game initialized
                input = new InputLayer(comm.width, comm.height, comm.players.toCharArray());
                String players = "";
                for (char player : input.players){
                    players += player;
                }
                broadcast(gson.toJson(new Comm("login", true, input.board.width, input.board.height, input.board.board, players, input.turn)));
            }
            default -> {
                webSocket.send(gson.toJson(new Comm("error", "Unknown Command!")));
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