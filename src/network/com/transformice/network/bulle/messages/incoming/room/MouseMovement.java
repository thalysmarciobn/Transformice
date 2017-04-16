package com.transformice.network.bulle.messages.incoming.room;

import com.transformice.helpers.Tokens;
import com.transformice.helpers.network.Incoming;
import com.transformice.helpers.network.Outgoing;
import com.transformice.network.bulle.messages.incoming.MessageHandler;

@Tokens(C = Incoming._4.C, CC = Incoming._4.movement)
public class MouseMovement extends MessageHandler {

    @Override
    public void handle() {
        if (this.client.logged) {
            int codePartie = this.packet.readInt();
            boolean droiteEnCours = this.packet.readBoolean();
            boolean gaucheEnCours = this.packet.readBoolean();
            int px = this.packet.readUnsignedShort();
            int py = this.packet.readUnsignedShort();
            int vx = this.packet.readUnsignedShort();
            int vy = this.packet.readUnsignedShort();
            boolean jump = this.packet.readBoolean();
            byte jump_img = this.packet.readByte();
            byte portal = this.packet.readByte();
            boolean isAngle = this.packet.bytesAvailable();
            int angle = isAngle ? this.packet.readUnsignedShort() : -1;
            int vel_angle = isAngle ? this.packet.readUnsignedShort() : -1;
            boolean loc1 = isAngle ? this.packet.readBoolean() : false;
            if (codePartie == this.client.room.lastCodePartie) {
                this.client.posX = px;
                this.client.posY = py;
                this.client.droiteEnCours = droiteEnCours;
                this.client.gaucheEnCours = gaucheEnCours;
                this.client.vx = vx;
                this.client.vy = vy;
                this.client.jump = jump;
                this.client.jump_img = jump_img;
                this.client.portal = portal;
                this.client.isAngle = isAngle;
                if (this.client.isAngle) {
                    this.client.angle = angle;
                    this.client.vel_angle = vel_angle;
                    this.client.loc1 = loc1;
                }
                if (droiteEnCours || gaucheEnCours) {
                    if (this.client.isAfk) {
                        this.client.isAfk = false;
                    }
                }
                this.packetManage.outgoing.get(Outgoing.player_movement).compose(this.bulle, this.client, this.packetID, this.packet);
            }
        }
    }
}