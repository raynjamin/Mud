package com.bensterrett.mud.entities;

import com.bensterrett.mud.area.Room;
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
    private Room room;
    private String name;
    private Connection connection;

    public User(String name, Connection connection) {
        this.name = name;
        this.connection = connection;

        Room.changeRooms(this, MudServer.world.getRooms().get(0));
        beginInputThread();
    }

    public void beginInputThread() {
        new ConnectionThread(connection, (conn) -> {
            while (true) {
                if (conn.isClosed()) break;

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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
