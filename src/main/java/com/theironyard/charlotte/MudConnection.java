package com.theironyard.charlotte;

import com.theironyard.charlotte.async.AsyncTask;
import com.theironyard.charlotte.async.InputQueueThread;
import com.theironyard.charlotte.commands.ActionTarget;
import com.theironyard.charlotte.commands.Global;
import com.theironyard.charlotte.commands.Interp;
import com.theironyard.charlotte.entities.User;

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
    private InputQueueThread inputThread;
    private BufferedReader in;
    private OutputStreamWriter out;
    private User user;
    private List<Consumer<String>> callbacks = new ArrayList<>();

    MudConnection(Socket socket) {
        this.socket = socket;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new OutputStreamWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputThread = new InputQueueThread(this);
        inputThread.start();

        Global.greetUser(this);
        // commandLoop();
    }



    private void sendLineToClient(String text) {
        if (!getSocket().isClosed()) {
            try {
                getOut().write(text);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loginUser() {
        AsyncTask.runAynsc(() -> {
            user = new User(inputThread.inputQueue.poll());
        });
    }


    private void beginInputLoop() {
        Runnable r = () -> {
            while (true) {
                String text = "";
                try {
                    if (!getSocket().isClosed()) {
                        text = in.readLine();

                        Interp.interpretCommand(this, text);
                    } else {
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

    public User getUser() {
        return user;
    }

    private String processActionAsString(MudConnection actor, MudConnection target, String firstPersonVerb, String thirdPersonVerb) { }

    public void send(String text) {
        sendLineToClient(text);
    }

    public void sendVerb(String text, String verbThirdPerson, String verbFirstPerson, ActionTarget location) {
        if (location == ActionTarget.WORLD) {
            Main.connections.forEach(mc -> {
                try {
                    if (!mc.getSocket().isClosed()) {
                        if (mc == this) {
                            mc.getOut().write(String.format("You %s %s\n", verbFirstPerson, text));
                        } else {
                            mc.getOut().write(String.format("%s %s %s\n", mc.name, verbThirdPerson, text));
                        }

                        mc.getOut().flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MudConnection that = (MudConnection) o;

        return socket != null ? socket.equals(that.socket) : that.socket == null;

    }

    @Override
    public int hashCode() {
        return socket != null ? socket.hashCode() : 0;
    }
}
