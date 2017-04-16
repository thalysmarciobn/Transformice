package com.transformice.network.bulle.messages.outgoing.room;

import com.transformice.bulle.Bulle;
import com.transformice.bulle.users.GameClient;
import com.transformice.helpers.network.ByteArray;
import com.transformice.helpers.network.Outgoing;
import com.transformice.network.bulle.messages.outgoing.MessageComposer;

public class PlayerCrouch extends MessageComposer {

    @Override
    public void compose(Bulle bulle, GameClient client, int packetID, ByteArray packet) {
        client.room.sendAllOthers(client, Outgoing.player_crouch, new ByteArray().writeInt(client.playerCode).writeByte(packet.readByte()).writeByte(0).toByteArray());
    }
}
