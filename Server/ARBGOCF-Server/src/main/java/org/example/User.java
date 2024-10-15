package org.example;

import java.net.InetSocketAddress;

public class User {
    String name;
    InetSocketAddress address;

    public User(String name, InetSocketAddress address) {
        this.name = name;
        this.address = address;
    }
}
