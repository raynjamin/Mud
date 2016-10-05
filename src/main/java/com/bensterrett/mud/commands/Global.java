package com.bensterrett.mud.commands;

import com.bensterrett.mud.area.Room;
import com.bensterrett.mud.entities.User;
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

        action.getActor().getRoom().getUsers().forEach((u) -> {
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

    public static void look(Action action) {
        User actor = action.getActor();
        Connection conn = action.getActor().getConnection();

        conn.sendLineToClient(actor.getRoom().getTitle());

        actor.getRoom().getUsers().forEach(u -> {
            if (!u.equals(actor)) {
                conn.sendLineToClient(String.format("%s is standing here.", u.getName()));
            }
        });
    }

    public static void north(Action action) {
        User actor = action.getActor();

        if (actor.getRoom().getNorth() != null) {
            Room.changeRooms(actor, actor.getRoom().getNorth());
        } else {
            actor.getConnection().sendLineToClient("You try to move north but you slam your face against the wall like an idiot.");
        }
    }

    public static void south(Action action) {
        User actor = action.getActor();

        if (actor.getRoom().getSouth() != null) {
            Room.changeRooms(actor, actor.getRoom().getSouth());
        } else {
            actor.getConnection().sendLineToClient("You try to move south but you slam your face against the wall like an idiot.");
        }
    }

    public static void east(Action action) {
        User actor = action.getActor();

        if (actor.getRoom().getEast() != null) {
            Room.changeRooms(actor, actor.getRoom().getEast());
        } else {
            actor.getConnection().sendLineToClient("You try to move east but you slam your face against the wall like an idiot.");
        }
    }

    public static void west(Action action) {
        User actor = action.getActor();

        if (actor.getRoom().getWest() != null) {
            Room.changeRooms(actor, actor.getRoom().getWest());
        } else {
            actor.getConnection().sendLineToClient("You try to move west but you slam your face against the wall like an idiot.");
        }
    }
}
