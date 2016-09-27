package com.theironyard.charlotte;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<MudConnection> concurrentSockets = new ArrayList<>();

    private static void initializeConnections(ServerSocket server) {
        Runnable r = () -> {
            try {
                while (true) {
                    MudConnection mc = new MudConnection(server.accept());

                    mc.getCallbacks().add(t -> {
                        Runnable r2 = () -> concurrentSockets.forEach(mc2 -> {
                            try {
                                mc2.getOut().write(String.format("From Server: %s\n", t));
                                mc2.getOut().flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                        new Thread(r2).start();
                    });

                    concurrentSockets.add(mc);
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
        concurrentSockets.forEach(mc -> {
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
