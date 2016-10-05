package com.bensterrett.mud.entities;

import com.bensterrett.mud.async.ConnectionThread;
import com.bensterrett.mud.commands.Action;
import com.bensterrett.mud.commands.Command;
import com.bensterrett.mud.server.Connection;
import com.bensterrett.mud.server.MudServer;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Created by Ben on 9/30/16.
 */
public class User {
    private String name;
    private Connection connection;

    public User(String name, Connection connection) {
        this.name = name;
        this.connection = connection;

        beginInputThread();
    }

    public void beginInputThread() {
        new ConnectionThread(connection, (conn) -> {
            while (true) {
                String text = conn.readLineFromClient();
                String[] words = text.split(" ");
                String[] rest = Arrays.copyOfRange(words, 1, words.length);

                Consumer<Action> function = Command.interpretCommand(words[0]);

                try {
                    MudServer.asyncCommandQueue.put(new Action(function, this, rest));
                } catch (InterruptedException e) {
                    MudServer.logger.severe("Interrupted while adding to async command queue.");
                }
            }
        }).start();
    }

    public String getName() {
        return name;
    }

    public Connection getConnection() {
        return connection;
    }
}
