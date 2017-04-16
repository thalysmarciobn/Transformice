package com.transformice.network.bulle.messages.incoming.chat;

import com.transformice.bulle.users.commands.Require;
import com.transformice.config.Config;
import com.transformice.helpers.Tokens;
import com.transformice.helpers.network.Incoming;
import com.transformice.network.bulle.messages.incoming.MessageHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Tokens(C = Incoming._6.C, CC = Incoming._6.command)
public class Command extends MessageHandler {

    @Override
    public void handle() {
        this.packet = this.packetManage.decrypt(this.packetID, this.packet, Config.transformice.packetKeys);
        String command = this.packet.readUTF();
        String[] values = StringUtils.split(command, " ");
        command = values[0].toLowerCase();
        String[] args = Arrays.copyOfRange(values, 1, values.length);
        if (this.client.gameManage.commandManage.commands.containsKey(command)) {
            com.transformice.bulle.users.commands.Command _command = this.client.gameManage.commandManage.commands.get(command);
            if (_command.getClass().getAnnotations().length > 0) {
                Require require = _command.getClass().getAnnotation(Require.class);
                if (args.length == require.Args() && this.client.privilegeLevel >= require.PrivilegeLevel()) {
                    _command.run(this.client, args);
                }
            }
        }
    }
}
