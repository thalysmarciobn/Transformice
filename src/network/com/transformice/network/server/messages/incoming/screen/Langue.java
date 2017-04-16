package com.transformice.network.server.messages.incoming.screen;

import com.transformice.helpers.network.Incoming;
import com.transformice.network.server.messages.incoming.MessageHandler;
import com.transformice.helpers.Tokens;

@Tokens(C = Incoming._8.C, CC = Incoming._8.langue)
public  class Langue extends MessageHandler {

    @Override
    public void handle() {
        byte langueID = this.packet.readByte();
        this.client.langueByte = langueID;
        this.client.langue = this.server.langues.getLangue(langueID);
    }
}