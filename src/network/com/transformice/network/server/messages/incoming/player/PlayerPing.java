package com.transformice.network.server.messages.incoming.player;

import com.transformice.helpers.network.ByteArray;
import com.transformice.helpers.network.Incoming;
import com.transformice.helpers.network.Outgoing;
import com.transformice.network.server.messages.incoming.MessageHandler;
import com.transformice.helpers.Tokens;

@Tokens(C = Incoming._8.C, CC = Incoming._8.ping)
public  class PlayerPing extends MessageHandler {

    @Override
    public void handle() {
        this.client.pingTimeMillis = System.currentTimeMillis();
        this.client.sendPacket(Outgoing.player_ping, new ByteArray().writeBytes(this.packet.toByteArray()).toByteArray());
    }
}