package com.theironyard.charlotte;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Ben on 9/26/16.
 */
public class MudConnection {
    private Socket socket;
    private BufferedReader in;
    private OutputStreamWriter out;

    private List<Consumer<String>> callbacks = new ArrayList<>();

    MudConnection(Socket socket) {
        this.socket = socket;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new OutputStreamWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        beginInputLoop();
    }

    private void beginInputLoop() {
        Runnable r = () -> {
            while (true) {
                String text = "";
                try {
                    text = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.printf("Received Message: %s", text);
                // java is kinda dumb like this.
                final String finalText = text;

                callbacks.forEach(cb -> cb.accept(finalText));
            }
        };

        new Thread(r).start();
    }

    public List<Consumer<String>> getCallbacks() {
        return callbacks;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getIn() {
        return in;
    }

    public OutputStreamWriter getOut() {
        return out;
    }

}
