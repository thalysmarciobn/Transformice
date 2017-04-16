package com.transformice.server;

import com.transformice.database.Database;
import com.transformice.helpers.StatsUtils;
import com.transformice.helpers.network.GeoIP;
import com.transformice.logging.Logging;
import com.transformice.network.server.messages.PacketManage;
import com.transformice.network.server.networking.GameServer;
import com.transformice.helpers.Langues;
import com.transformice.config.Config;
import com.transformice.server.users.GameManage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public Database database;
    public GameManage gameManage;
    public PacketManage packetManage;
    public GameServer gameServer;
    public Langues langues;
    public GeoIP geoIP;
    public long start;

    public List<String> bulles = new ArrayList<>();

    public void start() throws Exception {
        if (Config.transformice()) {
            Logging.print("Loading server...", "info");
            if (Config.gameserver()) {
                Logging.print("Database: Connecting...", "info");
                this.database = new Database();
                if (this.database.connect()) {
                    this.start = System.currentTimeMillis();
                    this.langues = new Langues();
                    this.gameManage = new GameManage(this);
                    Logging.print("PacketManage: Loading packets...", "info");
                    this.packetManage = new PacketManage(this);
                    Logging.print("GeoIP: Loading data...", "info");
                    this.geoIP = new GeoIP();
                    if (Config.netty()) {
                        this.gameServer = new GameServer(this, Config.gameserver.host, Config.gameserver.ports);
                        this.gameServer.initialise();
                        this.gameServer.bind();
                        Logging.print("Server started in " + (System.currentTimeMillis() - this.start) + "ms.", "info");
                        Logging.print("Server online on ports: " + this.gameServer.validPorts.toString(), "info");
                        if (Config.transformice.console) {
                            Console.init(this.gameManage);
                        }
                    }
                }
            }
        }
    }
}