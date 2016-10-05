package com.bensterrett.mud.commands;

import com.bensterrett.mud.server.Connection;
import com.bensterrett.mud.server.MudServer;

import java.util.Arrays;

/**
 * Created by Ben on 9/30/16.
 */
public class Global {
    public static void say(Action action) {
        Connection orig = action.getActor().getConnection();
        String sentence = Arrays.stream(action.getArguments())
                .reduce("", (a, b) -> {
                    if (a != "") {
                        return a.concat(" " + b);
                    } else {
                        return a.concat(b);
                    }
                });

        orig.sendLineToClient(String.format("You say \"%s\"", sentence));

        MudServer.users.forEach((s, u) -> {
            if (!u.equals(action.getActor())) {
                u.getConnection().sendLineToClient(String.format("%s says \"%s\"", action.getActor().getName(), sentence));
            }
        });
    }

    public static void notFound(Action action) {
        action.getActor().getConnection().sendLineToClient("Huh?");
    }

    public static void quit(Action action) {
        Connection conn = action.getActor().getConnection();
        conn.sendLineToClient("Goodbye for now!");

        MudServer.users.remove(action.getActor());
        conn.close();
    }
}
