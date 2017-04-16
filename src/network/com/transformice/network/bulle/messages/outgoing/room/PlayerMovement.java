package com.transformice.network.bulle.messages.outgoing.room;

import com.transformice.bulle.Bulle;
import com.transformice.bulle.users.GameClient;
import com.transformice.helpers.network.ByteArray;
import com.transformice.helpers.network.Outgoing;
import com.transformice.network.bulle.messages.outgoing.MessageComposer;

public class PlayerMovement extends MessageComposer {

    @Override
    public void compose(Bulle bulle, GameClient client, int packetID, ByteArray packet) {
        ByteArray packet2 = new ByteArray().writeInt(client.playerCode).writeInt(client.room.lastCodePartie).writeBoolean(client.droiteEnCours).writeBoolean(client.gaucheEnCours).writeShort(client.posX).writeShort(client.posY).writeShort(client.vx).writeShort(client.vy).writeBoolean(client.jump).writeByte(client.jump_img).writeByte(client.portal);
        if (client.isAngle) {
            packet2.writeShort(client.angle).writeShort(client.vel_angle).writeBoolean(client.loc1);
        }
        client.room.sendAllOthers(client, Outgoing.player_movement, packet2.toByteArray());
    }
}
