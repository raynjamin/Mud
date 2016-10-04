package com.bensterrett.mud.server;

import com.bensterrett.mud.entities.User;

import java.io.*;
import java.net.Socket;
import static com.bensterrett.mud.server.MudServer.*;

/**
 * Created by Ben on 9/30/16.
 */
public class Connection {
    private Socket socket;
    private BufferedReader reader;
    private OutputStreamWriter writer;

    public Connection(Socket socket) {
        this.socket = socket;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new OutputStreamWriter(socket.getOutputStream());
        } catch (IOException e) {
            testLogger.severe("Could not setup I/O for user socket.");
        }
    }

    public String readLineFromClient() {
        String line = "";

        try {
            line = reader.readLine();
        } catch (IOException e) {
            testLogger.severe("Failed to read input from client");
        }

        return line;
    }

    public void sendLineToClient(String line) {
        try {
            if (!socket.isClosed()) {
                writer.write(line);
                writer.flush();
            }
        } catch (IOException e) {
            testLogger.severe("Unable to send message to client");
        }
    }

    public User loginUser() {
        sendLineToClient("What is your name?");

        String name = readLineFromClient();

        return new User(name, this);
    }
}
