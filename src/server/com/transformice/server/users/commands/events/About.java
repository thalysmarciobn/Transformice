package com.transformice.server.users.commands.events;

import com.transformice.config.Config;
import com.transformice.helpers.StatsUtils;
import com.transformice.server.users.GameClient;
import com.transformice.server.users.commands.Command;
import com.transformice.server.users.commands.Require;

@Require(Args = 0, PrivilegeLevel = 0)
public class About implements Command {

    @Override
    public void run(GameClient client, String... args) {
        client.sendMessage("Emulator: \n"
                + "Players online: " + client.gameManage.playersLogged.size() + "\n"
                + "Active rooms: " + client.gameManage.rooms.size() + "\n"
                + "Connections: " + client.gameManage.connections.size() + "\n"
                + "MySQL Connections: " + client.gameManage.server.database.getActiveConnections() + "/" + Config.database.pool + "\n"
                + "Uptime: " + StatsUtils.millisecondsToDate(System.currentTimeMillis() - client.gameManage.server.start), false);
    }
}
