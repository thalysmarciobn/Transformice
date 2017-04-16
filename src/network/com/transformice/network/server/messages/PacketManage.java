package com.transformice.network.server.messages;

import com.transformice.logging.Logging;
import com.transformice.network.server.messages.incoming.chat.Command;
import com.transformice.network.server.messages.incoming.player.GameLog;
import com.transformice.network.server.messages.incoming.player.PlayerPing;
import com.transformice.server.Server;
import com.transformice.helpers.network.ByteArray;
import com.transformice.helpers.Tokens;
import com.transformice.network.server.messages.incoming.MessageHandler;
import com.transformice.network.server.messages.incoming.player.ComputerInfo;
import com.transformice.network.server.messages.incoming.player.KeepAlive;
import com.transformice.network.server.messages.incoming.screen.Captcha;
import com.transformice.network.server.messages.incoming.screen.Langue;
import com.transformice.network.server.messages.incoming.screen.Login;
import com.transformice.network.server.messages.incoming.screen.Screen;
import com.transformice.network.server.messages.outgoing.MessageComposer;
import com.transformice.helpers.network.Outgoing;
import com.transformice.network.server.messages.outgoing.screen.BannerLogin;
import com.transformice.network.server.messages.outgoing.screen.ImageLogin;
import com.transformice.network.server.messages.outgoing.screen.DrawCaptcha;
import com.transformice.network.server.messages.outgoing.screen.CorrectVersion;
import com.transformice.config.Config;
import com.transformice.server.users.GameClient;
import gnu.trove.map.hash.THashMap;

import java.util.List;

public class PacketManage {

    private final Server server;
    public final THashMap<Integer, MessageHandler> incoming;
    public final THashMap<int[], MessageComposer> outgoing;

    public PacketManage(Server server) {
        long start = System.currentTimeMillis();
        this.server = server;
        this.incoming = new THashMap();
        this.outgoing = new THashMap();
        this.registerIncoming(new Screen());
        this.registerIncoming(new Langue());
        this.registerIncoming(new ComputerInfo());
        this.registerIncoming(new KeepAlive());
        this.registerIncoming(new Captcha());
        this.registerIncoming(new Login());
        this.registerIncoming(new PlayerPing());
        this.registerIncoming(new GameLog());
        this.registerIncoming(new Command());

        this.registerOutgoing(Outgoing.correct_version, new CorrectVersion());
        this.registerOutgoing(Outgoing.banner_login, new BannerLogin());
        this.registerOutgoing(Outgoing.image_login, new ImageLogin());
        this.registerOutgoing(Outgoing.captcha, new DrawCaptcha());
        Logging.print("PacketManage: Incoming packets: " + this.incoming.size(), "info");
        Logging.print("PacketManage: Outgoing packets: " + this.outgoing.size(), "info");
        Logging.print("PacketManage: Loaded in : " + (System.currentTimeMillis() - start) + "ms.", "info");
    }

    public void handlePacket(GameClient client, ByteArray packet, int packetID) {
        int[] tokens = new int[] {packet.readByte(), packet.readByte()};
        int header = this.getHeader(tokens[0], tokens[1]);
        if (this.incoming.containsKey(header)) {
            try {
                MessageHandler messageHandler = this.incoming.get(header);
                messageHandler.packetManage = this;
                messageHandler.server = this.server;
                messageHandler.client = client;
                messageHandler.packet = packet;
                messageHandler.packetID = packetID;
                messageHandler.client.packetID = ++messageHandler.client.packetID % 100;
                messageHandler.handle();
            } catch (Exception e) {
                Logging.error(e);
            }
        } else {
            if (Config.transformice.debug) {
                Logging.print("Packet not found: [" + tokens[0] + ", " + tokens[1] + "]", "info");
            }
        }
    }

    private void registerIncoming(MessageHandler handler) {
        if (handler.getClass().getAnnotations().length > 0) {
            Tokens event = handler.getClass().getAnnotation(Tokens.class);
            this.incoming.putIfAbsent(this.getHeader(event.C(), event.CC()), handler);
        }
    }

    private void registerOutgoing(int[] tokens, MessageComposer handler) {
        this.outgoing.putIfAbsent(tokens, handler);
    }

    public int getHeader(int C, int CC) {
        return (C << 8) | CC;
    }

    public ByteArray decrypt(int packetID, ByteArray packet, List<Integer> keys) {
        ByteArray data = new ByteArray();
        while (packet.bytesAvailable()) {
            packetID = ++packetID % keys.size();
            data.writeByte(packet.readByte() ^ keys.get(packetID));
        }
        return data;
    }

}