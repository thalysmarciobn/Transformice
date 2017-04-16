package com.transformice.bulle.rooms.threads;

import com.transformice.bulle.rooms.Rooms;
import com.transformice.bulle.users.GameClient;

public class MapStart implements Runnable {

    private Rooms room;

    public MapStart(Rooms room) {
        this.room = room;
    }

    @Override
    public void run() {
        this.room.isCurrentlyPlay = true;
        for (GameClient client : this.room.players.values()) {
            client.sendMapStart(0);
        }
    }
}
