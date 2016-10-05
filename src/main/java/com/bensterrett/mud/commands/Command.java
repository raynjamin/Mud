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
        COMMAND_TABLE.put("say", Global::say);
        COMMAND_TABLE.put("quit", Global::quit);
    }

    public static Consumer<Action> interpretCommand(String commandText) {
        if (COMMAND_TABLE.containsKey(commandText)) {
            return COMMAND_TABLE.get(commandText);
        } else {
            Optional<String> command =
                    COMMAND_TABLE.keySet()
                        .stream()
                        .filter(c -> c.startsWith(commandText))
                        .findFirst();

            if (command.isPresent()) {
                // caching this lookup for future command lookup.
                COMMAND_TABLE.put(commandText, COMMAND_TABLE.get(command.get()));

                return COMMAND_TABLE.get(command.get());
            } else {
                return Global::notFound;
            }
        }

    }
}
