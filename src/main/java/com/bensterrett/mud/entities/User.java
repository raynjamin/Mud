package com.bensterrett.mud.entities;

import com.bensterrett.mud.server.Connection;

/**
 * Created by Ben on 9/30/16.
 */
public class User {
    private String name;
    private Connection connection;

    public User(String name, Connection connection) {
        this.name = name;
        this.connection = connection;
    }

    public String getName() {
        return name;
    }

    public Connection getConnection() {
        return connection;
    }
}
