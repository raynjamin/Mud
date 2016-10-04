package com.bensterrett.mud.server;

import com.bensterrett.mud.entities.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by Ben on 9/30/16.
 */
public class MudServer {
    public static Logger testLogger = Logger.getAnonymousLogger();
    public static Thread connectionsThread;
    public static Map<String, User> users = Collections.synchronizedMap(new HashMap<>());

    static {
        try {
            testLogger.addHandler(new FileHandler());
        } catch (IOException e) {
            testLogger.severe("FAILED TO INITIALIZE FILE HANDLER");
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(Integer.valueOf(args[0]));

        connectionsThread = beginAcceptConnectionsThread(server);
    }

    public static Thread beginAcceptConnectionsThread(ServerSocket server) {
        Runnable r = () -> {
            while (true) {
                try {
                    Connection conn = new Connection(server.accept());

                    User user = conn.loginUser();

                    users.put(user.getName(), user);

                } catch (IOException e) {
                    testLogger.severe("Unable to accept connection.");
                }
            }
        };

        Thread t = new Thread(r);

        t.start();

        return t;
    }
}
