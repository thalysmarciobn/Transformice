package com.transformice.network.server.messages.outgoing.screen;

import com.transformice.server.Server;
import com.transformice.network.server.messages.outgoing.MessageComposer;
import com.transformice.helpers.network.Outgoing;
import com.transformice.server.users.GameClient;

public class BannerLogin extends MessageComposer {

    @Override
    public void compose(Server server, GameClient client, int packetID) {
        client.sendPacket(Outgoing.banner_login, 58, 0);
    }
}
