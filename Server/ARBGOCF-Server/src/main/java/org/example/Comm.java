package org.example;

import java.util.LinkedList;

public class Comm {
    String type;
    String username;
    String message;
    LinkedList<User> users;
    int column;
    int width;
    int height;
    String players;
    char player;

    public Comm(String type, LinkedList<User> users) {
        this.type = type;
        this.users = users;
    }

    public Comm(String type, String message){
        this.type = type;
        this.message = message;
    }
}
