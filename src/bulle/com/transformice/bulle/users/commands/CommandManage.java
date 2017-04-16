package com.transformice.bulle.users.commands;

import com.transformice.bulle.users.commands.events.Mort;
import com.transformice.bulle.users.commands.events.Ping;
import gnu.trove.map.hash.THashMap;

public class CommandManage {

    public final THashMap<String, Command> commands;

    public CommandManage() throws Exception {
        this.commands = new THashMap();

        this.registerCommand("mort", new Mort());
        this.registerCommand("ping", new Ping());
    }

    public void registerCommand(String command, Command _command) {
        if (!this.commands.containsKey(command)) {
            this.commands.put(command, _command);
        }
    }
}