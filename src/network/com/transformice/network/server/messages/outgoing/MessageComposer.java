package com.transformice.network.server.messages.outgoing;

import com.transformice.helpers.network.ByteArray;
import com.transformice.server.Server;
import com.transformice.server.users.GameClient;

public abstract class MessageComposer {

    public abstract void compose(Server server, GameClient client, int packetID);
}
