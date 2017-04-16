package com.transformice.network.server.messages.incoming.screen;

import com.transformice.helpers.network.Incoming;
import com.transformice.network.server.messages.incoming.MessageHandler;
import com.transformice.helpers.Tokens;
import com.transformice.helpers.network.Outgoing;

@Tokens(C = Incoming._28.C, CC = Incoming._28.validate)
public  class Screen extends MessageHandler {

    @Override
    public void handle() {
        this.packetManage.outgoing.get(Outgoing.correct_version).compose(this.server, this.client, this.packetID);
        this.packetManage.outgoing.get(Outgoing.banner_login).compose(this.server, this.client, this.packetID);
        this.packetManage.outgoing.get(Outgoing.image_login).compose(this.server, this.client, this.packetID);
    }
}