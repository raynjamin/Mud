package com.bensterrett.mud.server;

import com.bensterrett.mud.commands.Action;
import com.bensterrett.mud.commands.Command;
import com.bensterrett.mud.commands.Global;
import com.bensterrett.mud.entities.User;
import sun.misc.Regexp;

import java.io.*;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            logger.severe("Could not setup I/O for user socket.");
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            MudServer.logger.severe(e.getMessage());
        }
    }

    public String readLineFromClient() {
        String line = "";

        try {
            if (!socket.isClosed()) {
                line = reader.readLine();
            }
        } catch (IOException e) {
            logger.info("Closing socket connection");
            close();
        }

        return line;
    }

    public void sendLineToClient(String line) {
        try {
            if (!socket.isClosed()) {
                writer.write(line + (line.endsWith("\n") ? "" : "\n"));
                writer.flush();
            }
        } catch (IOException e) {
            logger.severe("Unable to send message to client");
        }
    }

    public User loginUser() {
        String name = "";
        Pattern p = Pattern.compile("[A-Za-z]{3,}");
        Matcher m;

        do {
            sendLineToClient("What is your name?");
            name = readLineFromClient();

            m = p.matcher(name);

            if (!m.matches()) {
                sendLineToClient("Invalid name. A name must be at least three characters and can only contain the letters a-z.");
            }
        } while (!m.matches());

        return new User(name, this);
    }

    public void setSocket(Socket newSocket) {
        this.socket = newSocket;
    }

    public boolean isClosed() {
        return socket.isClosed();
    }
}
