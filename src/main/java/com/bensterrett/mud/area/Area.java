package com.bensterrett.mud.area;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 10/4/16.
 */
public class Area {
    private String title;
    private String description;

    private List<Area> areas = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();

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

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
