package com.transformice.network.bulle.messages.incoming.room;

import com.transformice.bulle.users.GameClient;
import com.transformice.helpers.Tokens;
import com.transformice.helpers.network.Incoming;
import com.transformice.network.bulle.messages.incoming.MessageHandler;

@Tokens(C = Incoming._4.C, CC = Incoming._4.mort)
public class Mort extends MessageHandler {

    @Override
    public void handle() {
        if (this.client.logged) {
            int codePartie = this.packet.readInt();
            if (this.client.room.lastCodePartie == codePartie) {
                this.client.isDead = true;
                if (!this.client.room.noAutoScore) this.client.playerScore++;
                this.client.sendPlayerDie();
                if (!this.client.room.currentShamanName.equals("")) {
                    GameClient client = this.client.room.players.get(this.client.room.currentShamanName);
                    if (client != null && !this.client.room.noShamanSkills) {
                        if (client.bubblesCount > 0) {
                            if (this.client.room.checkDeathCount()[1] != 1) {
                                client.bubblesCount--;
                                //this.client.sendPlaceObject(this.client.room.objectID + 2, 59, this.client.posX, 450, 0, 0, 0, true, true);
                            }
                        }
                        if (client.desintegration) {
                            //this.client.skillModule.sendSkillObject(6, this.client.posX, 395, 0);
                        }
                    }
                }
                this.client.room.checkShouldChangeMap();
            }
        }
    }
}