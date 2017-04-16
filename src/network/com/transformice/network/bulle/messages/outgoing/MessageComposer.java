package com.transformice.network.bulle.messages.outgoing;

import com.transformice.bulle.Bulle;
import com.transformice.bulle.users.GameClient;
import com.transformice.helpers.network.ByteArray;

public abstract class MessageComposer {

    public abstract void compose(Bulle bulle, GameClient client, int packetID, ByteArray packet);
}

