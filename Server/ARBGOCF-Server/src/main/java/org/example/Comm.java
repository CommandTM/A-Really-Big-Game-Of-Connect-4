package org.example;

import java.util.LinkedList;

public class Comm {
    String type;
    String username;
    String message;
    LinkedList<User> users;
    Integer column;
    Integer width;
    Integer height;
    String players;
    Boolean gameExists;
    LinkedList<String> board;
    Integer turn;

    /**
     * Construct a user list refresh packet
     * @param type Packet type
     * @param users List of usernames of the connected clients
     */
    public Comm(String type, LinkedList<User> users) {
        this.type = type;
        this.users = users;
    }

    /**
     * Construct an error message packet
     * @param type Packet type
     * @param message Packet information meant for display
     */
    public Comm(String type, String message){
        this.type = type;
        this.message = message;
    }

    /**
     * Construct a message packet
     * @param type Packet type
     * @param username Username of who sent this packet
     * @param message Packet information meant for display
     */
    public Comm(String type, String username, String message){
        this.type = type;
        this.username = username;
        this.message = message;
    }

    /**
     * Construct a login packet that has a game not existing
     * @param type Packet type
     * @param gameExists Whether there is an active game
     */
    public Comm(String type, boolean gameExists){
        this.type = type;
        this.gameExists = gameExists;
    }

    /**
     * Construct a login packet with all current game information needed
     * @param type Packet type
     * @param gameExists Whether there is an active game
     * @param width The width of the board
     * @param height The height of the board
     * @param board The Connect 4 board itself
     * @param players The list of players formatted as a single string
     * @param turn The current turn number
     */
    public Comm(String type, boolean gameExists, int width, int height, char[][] board, String players, int turn){
        this.type = type;
        this.gameExists = gameExists;
        LinkedList<String> transfer = new LinkedList<>();
        for (int i = 0; i < board.length; i++) {
            transfer.add("");
            for (int j = 0; j < board[i].length; j++) {
                transfer.set(i, transfer.get(i) + board[i][j] + " ");
            }
        }
        this.board = transfer;
        this.players = players;
        this.turn = turn;
        this.width = width;
        this.height = height;
    }
}
