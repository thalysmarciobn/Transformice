package com.transformice.bulle.users.commands;

import com.transformice.bulle.users.GameClient;

public interface Command {
    void run(GameClient client, String... args);
}