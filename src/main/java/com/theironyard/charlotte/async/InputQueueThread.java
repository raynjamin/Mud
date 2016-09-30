package com.theironyard.charlotte.async;

import com.theironyard.charlotte.MudConnection;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Ben on 9/30/16.
 */
public class InputQueueThread extends Thread {
    MudConnection mc;
    public final Queue<String> inputQueue = new LinkedBlockingQueue<>();

    public InputQueueThread(MudConnection mc) {
        this.mc = mc;
    }

    @Override
    public void run () {
        while (true) {
            if (!mc.getSocket().isClosed()) {
                inputQueue.add(readLineFromClient());
            }
        }
    }

    private String readLineFromClient() {
        String text = "";

        try {
            if (!mc.getSocket().isClosed()) {
                text = mc.getIn().readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return text;
    }
}
