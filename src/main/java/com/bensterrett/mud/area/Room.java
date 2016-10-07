package com.bensterrett.mud.area;

import com.bensterrett.mud.commands.Action;
import com.bensterrett.mud.commands.Global;
import com.bensterrett.mud.entities.User;
import com.bensterrett.mud.server.MudServer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ben on 10/4/16.
 */
public class Room {
    int id;
    String title;
    String description;
    Area area;
    Room north;
    Room south;
    Room east;
    Room west;
    Room up;
    Room down;

    public List<User> getUsers() {
        return MudServer.users.values().parallelStream()
                .filter(user -> user.getRoom().id == id)
                .collect(Collectors.toList());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Room getNorth() {
        return north;
    }

    public void setNorth(Room north) {
        this.north = north;
    }

    public Room getSouth() {
        return south;
    }

    public void setSouth(Room south) {
        this.south = south;
    }

    public Room getEast() {
        return east;
    }

    public void setEast(Room east) {
        this.east = east;
    }

    public Room getWest() {
        return west;
    }

    public void setWest(Room west) {
        this.west = west;
    }

    public Room getUp() {
        return up;
    }

    public void setUp(Room up) {
        this.up = up;
    }

    public Room getDown() {
        return down;
    }

    public void setDown(Room down) {
        this.down = down;
    }

    public static String exits(Room input) {
        String format = "[exits: %s%s%s%s%s%s";

        return String.format(format,
                input.getNorth() == null ? "" : "north ",
                input.getEast() == null ? "" : "east ",
                input.getSouth() == null ? "" : "south ",
                input.getWest() == null ? "" : "west ",
                input.getUp() == null ? "" : "up ",
                input.getDown() == null ? "" : "down ").trim() + "]";
    }

    public static void changeRooms(User actor, Room newRoom) {
        Room current = actor.getRoom();

        if (current != null) {
            current.getUsers().remove(actor);
        }

        actor.setRoom(newRoom);
        newRoom.getUsers().add(actor);

        // TODO: refactor this constructor.

        Global.look(new Action(null, actor, null));
    }
}
