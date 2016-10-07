package com.bensterrett.mud.server;

import com.bensterrett.mud.area.Area;
import com.bensterrett.mud.area.Room;
import com.bensterrett.mud.async.ConnectionThread;
import com.bensterrett.mud.commands.Action;
import com.bensterrett.mud.commands.Global;
import com.bensterrett.mud.entities.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by Ben on 9/30/16.
 */
public class MudServer {
    public static Logger logger = Logger.getAnonymousLogger();
    public static Map<String, User> users = Collections.synchronizedMap(new HashMap<>());
    public static LinkedBlockingQueue<Action> asyncCommandQueue = new LinkedBlockingQueue<>();
    public static Area world = new Area();

    static {
        try {
            logger.addHandler(new FileHandler());
        } catch (IOException e) {
            logger.severe("FAILED TO INITIALIZE FILE HANDLER");
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(Integer.valueOf(args[0]));

        // load up the world
        loadWorld();

        // spin up connections thread
        acceptConnections(server);

        // spin up command thread.
        processUserActions();
    }

    public static void loadWorld() {
        List<Room> rooms = world.getRooms();

        Room one = new Room();
        one.setTitle("Temple of Enoch");
        one.setDescription("This is a long description of things.");

        Room two = new Room();
        two.setTitle("Pit of Sacrifice");
        two.setDescription("Put your stuff here.");

        one.setEast(two);
        two.setWest(one);

        rooms.add(one);
        rooms.add(two);
    }

    public static void processUserActions() {
        new Thread(() -> {
            while (true) {
                Action a = null;

                try {
                    a = asyncCommandQueue.take();

                } catch (InterruptedException e) {
                    logger.severe("Could not pull action from queue");
                }

                a.getCommand().accept(a);
                a.getActor().getConnection().sendLineToClient(Room.exits(a.getActor().getRoom()));
            }
        }).start();
    }

    public static void acceptConnections(ServerSocket server) {
        new Thread(() -> {
            while (true) {
                try {
                    Connection conn = new Connection(server.accept());

                    new ConnectionThread(conn, c -> {
                        User newUser = c.loginUser();

                        if (users.containsKey(newUser.getName())) {
                            User existingUser = users.get(newUser.getName());

                            existingUser.getConnection().sendLineToClient("This body has been taken over!");
                            existingUser.getConnection().close();
                            existingUser.setConnection(conn);

                        } else {
                            users.put(newUser.getName(), newUser);
                        }

                        Global.motd(new Action(Global::motd, newUser, null));

                        c.sendLineToClient("Welcome To The Mud.");
                    }).start();

                } catch (IOException e) {
                    logger.severe("Unable to accept connection.");
                }
            }
        }).start();
    }
}
