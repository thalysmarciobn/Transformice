package com.transformice.network.server.messages.incoming.player;

import com.transformice.helpers.network.Incoming;
import com.transformice.network.server.messages.incoming.MessageHandler;
import com.transformice.helpers.Tokens;

@Tokens(C = Incoming._26.C, CC = Incoming._26.keepAlive)
public  class KeepAlive extends MessageHandler {

    @Override
    public void handle() {
    }
}