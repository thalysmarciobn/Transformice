package com.transformice.network.bulle.messages.incoming.room;

import com.transformice.helpers.Tokens;
import com.transformice.helpers.network.ByteArray;
import com.transformice.helpers.network.Incoming;
import com.transformice.helpers.network.Outgoing;
import com.transformice.network.bulle.messages.incoming.MessageHandler;

@Tokens(C = Incoming._4.C, CC = Incoming._4.object_sync)
public class ObjectSync extends MessageHandler {

    @Override
    public void handle() {
        if (this.client.logged) {
            int codePartie = this.packet.readInt();
            if (codePartie == this.client.room.lastCodePartie && this.client.isSync && this.client.room.players.size() >= 2) {
                ByteArray packet2 = new ByteArray();
                while (this.packet.bytesAvailable()) {
                    packet2.writeShort(this.packet.readShort());
                    short code = this.packet.readShort();
                    byte[] object = new byte[0];
                    if (code != -1) {
                        object = new byte[14];
                        this.packet.read(object);
                    }
                    packet2.writeShort(code).writeBytes(object);
                    if (code != -1) {
                        packet2.writeBoolean(true);
                    }
                }
                if ((((!this.client.room.changed20secTimer ? this.client.room.roundTime + this.client.room.addTime : 20) * 1000) + (this.client.room.gameStartTimeMillis - System.currentTimeMillis())) > 5000) {
                    this.client.room.sendAllOthers(this.client, Outgoing.object_sync, packet2.toByteArray());
                }
            }
        }
    }
}