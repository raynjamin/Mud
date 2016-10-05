package com.bensterrett.mud.commands;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by Ben on 10/4/16.
 */
public class Command {
    protected static final HashMap<String, Consumer<Action>> COMMAND_TABLE = new HashMap<>();

    static {
        COMMAND_TABLE.put("'", Global::say);
        COMMAND_TABLE.put("say", Global::say);
        COMMAND_TABLE.put("quit", Global::quit);
        COMMAND_TABLE.put("look", Global::look);
        COMMAND_TABLE.put("north", Global::north);
        COMMAND_TABLE.put("south", Global::south);
        COMMAND_TABLE.put("east", Global::east);
        COMMAND_TABLE.put("west", Global::west);
    }

    public static Consumer<Action> interpretCommand(String commandText) {
        String lowerCommandText = commandText.toLowerCase();

        if (COMMAND_TABLE.containsKey(lowerCommandText)) {
            return COMMAND_TABLE.get(lowerCommandText);
        } else {
            Optional<String> command =
                    COMMAND_TABLE.keySet()
                        .stream()
                        .filter(c -> c.startsWith(lowerCommandText))
                        .findFirst();

            if (command.isPresent()) {
                // caching this lookup for future command lookup.
                COMMAND_TABLE.put(lowerCommandText, COMMAND_TABLE.get(command.get()));

                return COMMAND_TABLE.get(command.get());
            } else {
                return Global::notFound;
            }
        }

    }
}
