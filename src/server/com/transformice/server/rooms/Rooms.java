package com.transformice.server.rooms;

import com.transformice.logging.Logging;
import com.transformice.config.Config;
import com.transformice.server.users.GameClient;
import com.transformice.server.users.GameManage;
import gnu.trove.map.hash.THashMap;

import java.util.Random;

public class Rooms {

    private final GameManage gameManage;
    private final THashMap<Integer, GameClient> clients;

    private final String name;
    private final String bulle;

    public Rooms(String name, GameManage gameManage) {
        this.name = name;
        this.gameManage = gameManage;
        this.clients = new THashMap();
        this.bulle = this.gameManage.server.bulles.get(new Random().nextInt(this.gameManage.server.bulles.size()));
        if (Config.transformice.debug) {
            Logging.print("New room with bulle: " + this.bulle + " [" + this.name + "]", "info");
        }
    }

    public String getName() {
        return this.name;
    }

    public String getBulle() {
        return this.bulle;
    }

    public int getPlayerCount() {
        return this.clients.size();
    }

    public void enterPlayer(GameClient client) {
        if (!this.clients.containsKey(client.playerCode)) {
            this.clients.put(client.playerCode, client);
            client.room = this;
        }
    }

    public void removePlayer(int playerCode) {
        if (this.clients.containsKey(playerCode)) {
            this.clients.remove(playerCode);
        }
        if (this.clients.size() <= 0) {
            this.gameManage.rooms.remove(this.name);
        }
    }
}