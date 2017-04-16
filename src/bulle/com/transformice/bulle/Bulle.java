package com.transformice.bulle;

import com.transformice.config.Config;
import com.transformice.network.bulle.messages.PacketManage;
import com.transformice.network.bulle.networking.GameServer;
import com.transformice.bulle.users.GameManage;
import com.transformice.database.Database;
import com.transformice.logging.Logging;

public class Bulle {
    public Database database;
    public GameManage sessionManager;
    public PacketManage packetManage;
    public GameServer gameServer;

    public Bulle() {
        try {
                Logging.print("Connecting to database...", "info");
                this.database = new Database();
                this.sessionManager = new GameManage(this);
                this.packetManage = new PacketManage(this);
                this.gameServer = new GameServer(this, Config.bulleserver.host, Config.bulleserver.ports);
        } catch (Exception e) {
            Logging.error(e);
        }
    }

    public void start() throws Exception {
        Logging.print("Loading bulle...", "info");
        if (Config.bulleserver()) {
            this.gameServer.initialise();
            this.gameServer.bind();
            Logging.print("Bulle online on ports: " + this.gameServer.validPorts.toString(), "info");
        }
    }
}
