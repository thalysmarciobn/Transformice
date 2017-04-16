package com.transformice.network.server.messages.incoming.screen;

import com.transformice.helpers.Tokens;
import com.transformice.helpers.network.Incoming;
import com.transformice.network.server.messages.incoming.MessageHandler;
import com.transformice.helpers.network.Outgoing;

@Tokens(C = Incoming._26.C, CC = Incoming._26.captcha)
public class Captcha extends MessageHandler {

    @Override
    public void handle() {
        this.client.currentCaptcha = this.client.gameManage.getRandom(4);
        this.packetManage.outgoing.get(Outgoing.captcha).compose(this.server, this.client, this.packetID);
    }
}
