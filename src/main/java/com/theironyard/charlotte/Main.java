package com.theironyard.charlotte;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final Map<String, MudConnection> connections = Collections.synchronizedMap(new HashMap<>());

    private static void initializeConnections(ServerSocket server) {
        Runnable r = () -> {
            try {
                while (true) {
                    MudConnection mc = new MudConnection(server.accept());
                    connections.put("")
                    System.out.println("Person Connected.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        new Thread(r).start();
    }

    private static void heartBeat(ServerSocket server) throws InterruptedException {
        while (true) {
            Thread.sleep(10);
        }
    }

    private static void broadcastText(String input) {
        connections.forEach(mc -> {
            Runnable r = () -> {
                try {
                    mc.getOut().write(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };

            new Thread(r).start();
        });
    }

    public static void main(String[] args) throws IOException, InterruptedException {
	// write your code here
        System.out.println("Starting Mud");
        ServerSocket mud = new ServerSocket(12345);

        initializeConnections(mud);
        heartBeat(mud);
    }
}
