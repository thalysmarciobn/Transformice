package com.transformice.bulle.users.commands.events;

import com.transformice.bulle.users.commands.Command;
import com.transformice.bulle.users.GameClient;
import com.transformice.bulle.users.commands.Require;

@Require(Args = 0, PrivilegeLevel = 0)
public class Mort implements Command {

    @Override
    public void run(GameClient client, String... args) {
        if (!client.isAfk) {
            client.sendPlayerDie();
            client.room.checkShouldChangeMap();
        }
    }
}
