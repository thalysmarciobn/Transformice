package com.transformice.bulle.rooms.threads;

import com.transformice.bulle.rooms.Rooms;
import com.transformice.bulle.users.GameClient;

public class KillAllAFK implements Runnable {

    private Rooms room;

    public KillAllAFK(Rooms room) {
        this.room = room;
    }

    @Override
    public void run() {
        if (!this.room.isEditeur || !this.room.isTotemEditeur || !this.room.isBootcamp || !this.room.isTribeHouseMap) {
            for (GameClient client : this.room.players.values()) {
                if (!client.isDead && client.isAfk) {
                    client.isDead = true;
                    client.sendPlayerDie();
                    if (!this.room.noAutoScore) {
                        client.playerScore++;
                    }
                }
            }
            this.room.checkShouldChangeMap();
        }
    }
}
