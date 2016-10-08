package com.bensterrett.mud.commands;

import com.bensterrett.mud.entities.User;

import java.util.function.Consumer;

/**
 * Created by Ben on 10/4/16.
 */
public class Action {
    private Consumer<Action> command;
    private User actor;
    private String[] arguments;

    public Action(Consumer<Action> command, User actor, String[] arguments) {
        this.command = command;
        this.actor = actor;
        this.arguments = arguments;
    }

    public void 56runAction() {
        command.accept(this);
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public Consumer<Action> getCommand() {
        return command;
    }

    public void setCommand(Consumer<Action> command) {
        this.command = command;
    }

    public User getActor() {
        return actor;
    }

    public void setActor(User actor) {
        this.actor = actor;
    }
}
