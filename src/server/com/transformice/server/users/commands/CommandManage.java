package com.transformice.server.users.commands;

import com.transformice.server.users.commands.events.About;
import gnu.trove.map.hash.THashMap;

public class CommandManage {

    public final THashMap<String, Command> commands;

    public CommandManage() throws Exception {
        this.commands = new THashMap();

        this.registerCommand("about", new About());
    }

    public void registerCommand(String command, Command _command) {
        if (!this.commands.containsKey(command)) {
            this.commands.put(command, _command);
        }
    }
}