package com.transformice.network.bulle.messages;

import com.transformice.bulle.Bulle;
import com.transformice.config.Config;
import com.transformice.helpers.network.Outgoing;
import com.transformice.network.bulle.messages.incoming.MessageHandler;
import com.transformice.network.bulle.messages.incoming.chat.Command;
import com.transformice.network.bulle.messages.incoming.player.BulleID;
import com.transformice.network.bulle.messages.incoming.player.KeepAlive;
import com.transformice.network.bulle.messages.incoming.player.PlayerPing;
import com.transformice.network.bulle.messages.incoming.room.Crouch;
import com.transformice.network.bulle.messages.incoming.room.Mort;
import com.transformice.network.bulle.messages.incoming.room.MouseMovement;
import com.transformice.network.bulle.messages.incoming.room.ObjectSync;
import com.transformice.network.bulle.messages.outgoing.MessageComposer;
import com.transformice.bulle.users.GameClient;
import com.transformice.helpers.network.ByteArray;
import com.transformice.helpers.Tokens;
import com.transformice.logging.Logging;
import com.transformice.network.bulle.messages.outgoing.room.PlayerCrouch;
import com.transformice.network.bulle.messages.outgoing.room.PlayerMovement;
import gnu.trove.map.hash.THashMap;

import java.util.List;

public class PacketManage {

    private final Bulle bulle;
    public final THashMap<Integer, MessageHandler> incoming;
    public final THashMap<int[], MessageComposer> outgoing;

    public PacketManage(Bulle bulle) {
        long start = System.currentTimeMillis();
        this.bulle = bulle;
        this.incoming = new THashMap();
        this.outgoing = new THashMap();

        this.registerIncoming(new BulleID());
        this.registerIncoming(new PlayerPing());
        this.registerIncoming(new MouseMovement());
        this.registerIncoming(new Mort());
        this.registerIncoming(new Crouch());
        this.registerIncoming(new ObjectSync());
        this.registerIncoming(new KeepAlive());
        this.registerIncoming(new Command());

        this.registerOutgoing(Outgoing.player_movement, new PlayerMovement());
        this.registerOutgoing(Outgoing.player_crouch, new PlayerCrouch());
        Logging.print("- Incoming packets: " + this.incoming.size(), "info");
        Logging.print("- Outgoing packets: " + this.outgoing.size(), "info");
        Logging.print("PacketManage loaded in : " + (System.currentTimeMillis() - start) + "ms.", "info");
    }

    public void handlePacket(GameClient client, ByteArray packet, int packetID) {
        int[] tokens = new int[] {packet.readByte(), packet.readByte()};
        int header = this.getHeader(tokens[0], tokens[1]);
        if (this.incoming.containsKey(header)) {
            try {
                final MessageHandler messageHandler = this.incoming.get(header);
                messageHandler.packetManage = this;
                messageHandler.bulle = this.bulle;
                messageHandler.client = client;
                messageHandler.packet = packet;
                messageHandler.packetID = packetID;
                messageHandler.handle();
            } catch (Exception e) {
                Logging.error(e);
            }
        } else {
            if (Config.transformice.debug) {
                Logging.print("Packet not found: [" + tokens[0] + ", " + tokens[1] + "]", "warn");
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