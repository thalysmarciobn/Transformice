package com.transformice.server.users;

import com.transformice.helpers.network.ByteArray;
import com.transformice.helpers.network.Outgoing;
import com.transformice.server.rooms.Rooms;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import java.net.InetSocketAddress;

public class GameClient {
    private Channel channel;
    public Rooms room;
    public GameManage gameManage;

    public int packetID = 0;
    public int authKey;
    public int playerID;
    public int playerCode;
    public int privilegeLevel;

    public long pingTimeMillis;

    public boolean isGuest;
    public boolean logged;

    public byte langueByte;

    public String username;
    public String roomName;
    public String link;
    public String langue;
    public String playerLangue;
    public String ipAddress;
    public String currentCaptcha;
    public String look;
    public String shamanLook;
    public String color;
    public String shamanColor;

    public GameClient(Channel channel, GameManage gameManage) {
        this.channel = channel;
        this.gameManage = gameManage;
        this.ipAddress = ((InetSocketAddress) channel.getRemoteAddress()).getAddress().getHostAddress();
        this.langue = this.gameManage.server.geoIP.getCountryCode(this.ipAddress);
    }

    public void sendPacket(int[] tokens, byte... data) {
        if (this.channel.isWritable()) {
            ByteArray packet = new ByteArray();
            int length = data.length + 2;
            if (length <= 0xFF) {
                packet.writeByte(1).writeByte(length);
            } else if (length <= 0xFFFF) {
                packet.writeByte(2).writeShort(length);
            } else if (length <= 0xFFFFFF) {
                packet.writeByte(3).writeByte((length >> 16) & 0xFF).writeByte((length >> 8) & 0xFF).writeByte(length & 0xFF);
            }
            packet.writeByte(tokens[0]).writeByte(tokens[1]).writeBytes(data);
            this.channel.write(ChannelBuffers.wrappedBuffer(packet.toByteArray()));
        }
    }

    public final void sendPacket(int[] tokens, int... data) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) data[i];
        }
        this.sendPacket(tokens, result);
    }

    public void dispose() {
        if (this.logged) {
            this.room.removePlayer(this.playerCode);
            this.gameManage.playersLogged.remove(this.username);
            this.gameManage.players.remove(this.playerCode);
        }
        this.channel.close();
    }

    public void enterPlayer(String startRoom) {
        this.gameManage.server.database.query("UPDATE users SET CountryCode = ? WHERE Username = ?", this.langue, this.username);
        this.roomName = startRoom == "" ? "BR-thalys" : this.langue + "-" + startRoom;
        this.playerCode = this.gameManage.playerCode++;
        if (this.isGuest) {
            this.sendPacket(Outgoing.login_souris, new ByteArray().writeByte(1).writeByte(10).toByteArray());
            this.sendPacket(Outgoing.login_souris, new ByteArray().writeByte(2).writeByte(5).toByteArray());
            this.sendPacket(Outgoing.login_souris, new ByteArray().writeByte(3).writeByte(15).toByteArray());
            this.sendPacket(Outgoing.login_souris, new ByteArray().writeByte(4).writeByte(200).toByteArray());
        }
        this.sendPacket(Outgoing.player_identification, new ByteArray().writeInt(this.playerID).writeUTF(this.username).writeInt(600000).writeByte(this.langueByte).writeInt(this.playerCode).writeByte(this.privilegeLevel).writeByte(0).writeBoolean(false).toByteArray());
        this.sendPacket(Outgoing.time_stamp, new ByteArray().writeInt(this.getTime()).toByteArray());
        this.sendPacket(Outgoing.email_confirmed, 1);
        this.gameManage.enterRoom(this.roomName, this);
        this.gameManage.players.put(this.playerCode, this);
        this.gameManage.playersLogged.add(this.username);
        this.sendPacket(Outgoing.bulle, new ByteArray().writeInt(this.playerCode).writeUTF(this.gameManage.rooms.get(this.roomName).getBulle()).toByteArray());
    }

    public int getTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public void sendMessage(String message, boolean toTab) {
        this.sendPacket(Outgoing.recv_message, new ByteArray().writeBoolean(toTab).writeUTF(message).writeByte(0).writeUTF("").toByteArray());
    }
}
