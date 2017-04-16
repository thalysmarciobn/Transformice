package com.transformice.network.bulle.messages.incoming.room;

import com.transformice.helpers.Tokens;
import com.transformice.helpers.network.Incoming;
import com.transformice.helpers.network.Outgoing;
import com.transformice.network.bulle.messages.incoming.MessageHandler;

@Tokens(C = Incoming._4.C, CC = Incoming._4.crouch)
public class Crouch extends MessageHandler {

    @Override
    public void handle() {
        if (this.client.logged) {
            this.packetManage.outgoing.get(Outgoing.player_crouch).compose(this.bulle, this.client, this.packetID, this.packet);
        }
    }
}