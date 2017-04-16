package com.transformice.network.server.messages.outgoing.screen;

import com.transformice.helpers.network.ByteArray;
import com.transformice.server.Server;
import com.transformice.network.server.messages.outgoing.MessageComposer;
import com.transformice.helpers.network.Outgoing;
import com.transformice.server.users.GameClient;

public class ImageLogin extends MessageComposer {

    @Override
    public void compose(Server server, GameClient client, int packetID) {
        client.sendPacket(Outgoing.image_login, new ByteArray().writeUTF("x_noel2014.jpg").toByteArray());
    }
}
