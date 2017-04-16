package com.transformice;

import com.transformice.server.Server;

public class JServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.start();
    }
}
