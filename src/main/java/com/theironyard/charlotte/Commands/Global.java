package com.theironyard.charlotte.commands;

import com.theironyard.charlotte.Main;
import com.theironyard.charlotte.MudConnection;

import java.io.IOException;

/**
 * Created by Ben on 9/27/16.
 */
public class Global {
    public static void greetUser(MudConnection actor, String... args) {
        actor.send(String.format("Welcome to The Mud, %s.", actor.getUser().getUserName()));
    }

    public static void quit(MudConnection actor, String... args) {
        try {
            actor.getSocket().close();
            Main.connections.remove(actor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void say(MudConnection actor, String... args) {
        StringBuilder sb = new StringBuilder();

        sb.append("\"");
        for (String s: args) {
            sb.append(s);
        }
        sb.append("\"");

        actor.sendVerb(sb.toString(), "says", "say", ActionTarget.WORLD);
    }
}
