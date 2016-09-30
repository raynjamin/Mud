package com.theironyard.charlotte.commands;

import com.theironyard.charlotte.MudConnection;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Created by Ben on 9/29/16.
 */
public class Command {
    private static final Map<String, Command> interpTable = Collections.synchronizedMap(new HashMap<>());
    private String command;
    private String firstPersonText;
    private String thirdPersonText;
    private BiConsumer<MudConnection, String[]> function;

    private Command(String command, String firstPersonText, String thirdPersonText, BiConsumer<MudConnection, String[]> function) {
        this.command = command;
        this.firstPersonText = firstPersonText;
        this.thirdPersonText = thirdPersonText;
        this.function = function;
    }

    private static Command findCommand(String input) {

    }

    public static void registerCommand(String[] aliases, BiConsumer action, String firstPersonText, String thirdPersonText) {
        Arrays.stream(aliases)
                .forEach(a ->
                    interpTable.put(a, new Command(a, firstPersonText, thirdPersonText, action)));
    }

    public static void interpretCommand(MudConnection actor, String text) {
        String[] words = text.split(" ");
        String command = words[0];


    }
}
