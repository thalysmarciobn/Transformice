package com.transformice.network.server.messages.outgoing.screen;

import com.transformice.helpers.network.ByteArray;
import com.transformice.server.Server;
import com.transformice.network.server.messages.outgoing.MessageComposer;
import com.transformice.helpers.network.Outgoing;
import com.transformice.server.users.GameClient;

import java.util.concurrent.ThreadLocalRandom;

public class CorrectVersion extends MessageComposer {

    @Override
    public void compose(Server server, GameClient client, int packetID) {
        client.packetID = ThreadLocalRandom.current().nextInt(99);
        client.authKey = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
        client.sendPacket(Outgoing.correct_version, new ByteArray().writeInt(server.gameManage.players.size()).writeByte(client.packetID).writeUTF(client.langue.toLowerCase()).writeUTF(client.langue.toLowerCase()).writeInt(client.authKey).toByteArray());
    }
}
