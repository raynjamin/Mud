package com.theironyard.charlotte.async;

/**
 * Created by Ben on 9/29/16.
 */
public class AsyncTask {
    public static void runAynsc(Runnable function) {
        new Thread(function).start();
    }
}
