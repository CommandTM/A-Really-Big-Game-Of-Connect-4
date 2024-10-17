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

    public Comm(String type, LinkedList<User> users) {
        this.type = type;
        this.users = users;
    }

    public Comm(String type, String message){
        this.type = type;
        this.message = message;
    }

    public Comm(String type, String username, String message){
        this.type = type;
        this.username = username;
        this.message = message;
    }

    public Comm(String type, boolean gameExists){
        this.type = type;
        this.gameExists = gameExists;
    }

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
