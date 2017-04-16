package com.transformice.server.users.commands;

import com.transformice.server.users.GameClient;

public interface Command {
    void run(GameClient client, String... args);
}