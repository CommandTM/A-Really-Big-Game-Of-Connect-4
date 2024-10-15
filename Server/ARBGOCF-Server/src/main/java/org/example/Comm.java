package org.example;

import java.util.LinkedList;

public class Comm {
    String type;
    String username;
    String message;
    LinkedList<User> users;

    public Comm(String type, LinkedList<User> users) {
        this.type = type;
        this.users = users;
    }
}
