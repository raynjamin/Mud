package com.theironyard.charlotte.commands;

import com.theironyard.charlotte.MudConnection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Created by Ben on 9/27/16.
 */
public class Interp {
    private static final Map<String, BiConsumer<MudConnection, String[]>> COMMANDS = new HashMap<>();

    static {
        COMMANDS.put("quit", Global::quit);
        COMMANDS.put("say", Global::say);
    }

    public static void interpretCommand(MudConnection mc, String input) {
        String[] args = input.split(" ");
        String[] rest = Arrays.copyOfRange(args, 1, args.length);

        if (input.equals("")) {
            return;
        }

        if (COMMANDS.containsKey(args[0])) {
            COMMANDS.get(args[0]).accept(mc, rest);
        } else {
            // TODO: Cache this command lookup.
            Optional<String> entry = COMMANDS.keySet()
                    .parallelStream()
                    .filter(k -> k.startsWith(args[0]))
                    .findFirst();

            if (entry.isPresent()) {
                COMMANDS.get(entry.get()).accept(mc, rest);
            }
        }
    }
}
