package com.transformice.network.server.messages.incoming.player;

import com.transformice.helpers.network.Incoming;
import com.transformice.network.server.messages.incoming.MessageHandler;
import com.transformice.helpers.Tokens;

@Tokens(C = Incoming._28.C, CC = Incoming._28.computer_info)
public  class ComputerInfo extends MessageHandler {

    @Override
    public void handle() {
        this.client.playerLangue = this.packet.readUTF().toUpperCase();
    }
}