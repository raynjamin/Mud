package com.bensterrett.mud.async;

import com.bensterrett.mud.server.Connection;

import java.util.function.Consumer;

/**
 * Created by Ben on 10/4/16.
 */
public class ConnectionThread extends Thread {
    Consumer<Connection> func;
    Connection conn;

    public ConnectionThread(Connection conn, Consumer<Connection> func) {
        this.conn = conn;
        this.func = func;
    }

    @Override
    public void run() {
        func.accept(conn);

        super.run();
    }
}
