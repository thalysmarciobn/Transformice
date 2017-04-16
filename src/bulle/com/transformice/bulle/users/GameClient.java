package com.transformice.bulle.users;

import com.sun.deploy.util.ArrayUtil;
import com.transformice.bulle.rooms.Rooms;
import com.transformice.helpers.network.ByteArray;
import com.transformice.helpers.network.Outgoing;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

public class GameClient {
    private Channel channel;
    public GameManage gameManage;
    public Rooms room;

    public int playerCode;
    public int playerID;
    public int bubblesCount;
    public int defilantePoints;
    public int currentPlace;
    public int iceCount = 2;
    public int playerScore = 0;
    public int playerStartTime;
    public int privilegeLevel;

    public int posX = 0;
    public int posY = 0;
    public int vx = 0;
    public int vy = 0;
    public int angle = 0;
    public int vel_angle = 0;
    public boolean droiteEnCours = false;
    public boolean gaucheEnCours = false;
    public boolean jump = false;
    public boolean isAngle = false;
    public boolean loc1 = false;
    public byte jump_img = 0;
    public byte portal = 0;

    public long pingTime = 0;
    public long playerStartTimeMillis;

    public boolean isDead;
    public boolean hasCheese;
    public boolean hasEnter;
    public boolean isShaman;
    public boolean isAfk;
    public boolean isSync;
    public boolean UTotem;
    public boolean canShamanRespawn;
    public boolean isOpportunist;
    public boolean desintegration;
    public boolean canMeep;
    public boolean isNewPlayer;
    public boolean isVampire;
    public boolean logged = false;

    public String username;
    public String look;
    public String shamanLook;
    public String color;
    public String shamanColor;
    public String title;
    public String langue;

    public GameClient(Channel channel, GameManage gameManage) {
        this.channel = channel;
        this.gameManage = gameManage;
    }

    public void dispose() {
        if (this.room != null) this.room.removePlayer(this);
        this.channel.setAttachment(null);
        this.channel.close();
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

    public void sendPacket(int[] identifiers, String packet) {
        this.sendPacket(identifiers, packet.getBytes());
    }

    public final void sendPacket(int[] tokens, int... data) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) data[i];
        }
        this.sendPacket(tokens, result);
    }

    public void sendOldPacket(int[] tokens, Object... values) {
        if (this.channel.isWritable()) {
            ByteArray packet = new ByteArray();
            String data = values.length == 0 ? "" : '\u0001' + StringUtils.join(values, '\u0001');
            int length = data.length() + 6;
            if (length <= 0xFF) {
                packet.writeByte(1).writeByte(length);
            } else if (length <= 0xFFFF) {
                packet.writeByte(2).writeShort(length);
            } else if (length <= 0xFFFFFF) {
                packet.writeByte(3).writeByte((length >> 16) & 0xFF).writeByte((length >> 8) & 0xFF).writeByte(length & 0xFF);
            }
            packet.writeByte(1).writeByte(1).writeShort(data.length() + 2).writeByte(tokens[0]).writeByte(tokens[1]).writeBytes(data);
            this.channel.write(ChannelBuffers.wrappedBuffer(packet.toByteArray()));
        }
    }

    public void resetPlay() {
        this.isSync = false;
        this.isDead = false;
        this.isAfk = true;
        this.isShaman = false;
        this.hasCheese = false;
        this.hasEnter = false;
        this.UTotem = false;
        this.canShamanRespawn = false;
        this.bubblesCount = 0;
        this.iceCount = 2;
        this.isOpportunist = false;
        this.desintegration = false;
        this.canMeep = false;
        this.defilantePoints = 0;
        this.isNewPlayer = false;
        this.currentPlace = 0;
        this.isVampire = false;
    }

    public void startPlay() {
        this.playerStartTime = this.getTime();
        this.playerStartTimeMillis = this.room.gameStartTimeMillis;
        this.isNewPlayer = this.room.isCurrentlyPlay;
        this.isDead = this.room.isCurrentlyPlay;
        this.sendNewMap(this.room.currentMap);
        this.sendPlayerList();

        this.sendCanTransformation(!this.room.isEditeur & ArrayUtils.contains(this.room.transformation, this.room.currentMap));

        int sync = this.room.currentSyncCode;
        this.sendSync(sync);
        if (this.playerCode == sync) {
            this.isSync = true;
        }

        this.sendRoundTime(this.room.roundTime + (this.room.gameStartTime - this.getTime()) + this.room.addTime);

        if (this.room.isCurrentlyPlay || this.room.isEditeur || this.room.isTutorial || this.room.isTotemEditeur || this.room.isBootcamp || this.room.isDefilante) {
            this.sendMapStart(0);
        } else {
            this.sendMapStart(1);
        }
        if (!this.room.isTutorial && !this.room.isVillage && !this.room.is801Room && !this.room.isTribeHouse && !this.room.isTribeHouseMap){
            this.sendPacket(new int[] {5, 51}, new ByteArray().writeByte(3).writeByte(-18).writeByte(1).writeShort(54).writeShort(-100).toByteArray());
        }
    }

    public int getTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    private void sendNewMap(int mapNum) {
        this.sendPacket(Outgoing.new_map, new ByteArray().writeInt(mapNum).writeShort(this.room.players.size()).writeByte(this.room.lastCodePartie).writeUTF("").writeUTF("").writeByte(0).writeByte(0).toByteArray());
    }

    private void sendPlayerList() {
        this.sendOldPacket(Outgoing.player_list, this.room.getPlayerList());
    }

    public void sendSync(int playerCode) {
        this.sendOldPacket(Outgoing.send_sync, (this.room.mapCode != -1 || this.room.EMapCode != 0) ? new Object[]{playerCode, ""} : new Object[]{playerCode});
    }

    public void sendRoundTime(int time) {
        this.sendPacket(Outgoing.round_time, new ByteArray().writeShort(time).toByteArray());
    }

    public void sendMapStart(int open) {
        if (this.hasCheese) {
            this.hasCheese = false;
            this.room.sendAll(Outgoing.remove_cheese, new ByteArray().writeInt(this.playerCode).toByteArray());
        }

        this.sendPacket(Outgoing.map_start_timer, open);
    }

    public String getPlayerData() {
        return StringUtils.join(new Object[]{this.username, this.playerCode, 1, this.isDead ? 1 : 0, this.playerScore, this.hasCheese ? 1 : 0, this.title, 0, !this.room.isBootcamp ? this.look : "1;0,0,0,0,0,0,0,0,0", 0, this.color, this.shamanColor, 0}, "#");
    }

    public void enterRoom(String roomName) {
        int gameMode = roomName.startsWith("music") ? 11 : roomName.contains("madchees") ? 1 : 4;
        int serverGame = roomName.contains("madchees") ? 4 : 0;
        roomName = roomName.replace("<", "&lt;");
        if (!roomName.startsWith("*") && !(roomName.length() > 3 && roomName.charAt(2) == '-')) {
            roomName = this.langue + "-" + roomName;
        }
        this.sendPacket(Outgoing.room_game_mode, serverGame);
        this.sendPacket(Outgoing.room_type, gameMode);
        this.sendPacket(Outgoing.enter_room, new ByteArray().writeBoolean(roomName.startsWith("*") || roomName.startsWith(String.valueOf((char) 3))).writeUTF(roomName).toByteArray());
    }

    public void sendPlayerDie() {
        this.room.sendAllOld(Outgoing.player_die, this.playerCode, 0, this.playerScore);
        this.hasCheese = false;

        if (this.room.players.size() >= 1) {
            if (this.room.isDoubleMap && !this.canShamanRespawn && this.room.checkIfDoubleShamansAreDead()) {
                this.room.send20SecRemainingTimer();

            } else if (this.room.checkIfShamanIsDead() && !this.canShamanRespawn) {
                this.room.send20SecRemainingTimer();
            }

            if (this.room.checkIfTooFewRemaining() && !this.canShamanRespawn) {
                this.room.send20SecRemainingTimer();
            }
        }

        if (this.room.checkDeathCount()[1] < 1 || this.room.catchTheCheeseMap || this.isAfk) {
            this.canShamanRespawn = false;
        }

        if (this.canShamanRespawn) {
            this.isDead = false;
            this.isAfk = false;
            this.hasCheese = false;
            this.hasEnter = false;
            this.canShamanRespawn = false;
            this.playerStartTime = this.getTime();
            this.playerStartTimeMillis = System.currentTimeMillis();
            this.room.sendAllOld(Outgoing.player_respawn, this.getPlayerData(), 0);
            for (GameClient client : this.room.players.values()) {
                client.sendShamanCode(this.playerCode, 0);
            }
        }
    }

    public void sendShamanCode(int shamanCode, int shamanCode2) {
        this.sendShamanCode(shamanCode, shamanCode2, this.room.currentShamanType, this.room.currentSecondShamanType, this.room.currentShamanLevel, this.room.currentSecondShamanLevel, this.room.currentShamanBadge, this.room.currentSecondShamanBadge);
    }

    private void sendShamanCode(int shamanCode, int shamanCode2, int shamanType, int shamanType2, int shamanLevel, int shamanLevel2, int shamanBadge, int shamanBadge2) {
        this.sendPacket(Outgoing.shaman_info, new ByteArray().writeInt(shamanCode).writeInt(shamanCode2).writeByte(shamanType).writeByte(shamanType2).writeShort(shamanLevel).writeShort(shamanLevel2).writeShort(shamanBadge).writeShort(shamanBadge2).toByteArray());
    }

    public void sendCanTransformation(boolean isTransformation) {
        this.sendPacket(Outgoing.can_transformation, new ByteArray().writeBoolean(isTransformation).toByteArray());
    }

    public void sendMessage(String message, boolean toTab) {
        this.sendPacket(Outgoing.recv_message, new ByteArray().writeBoolean(toTab).writeUTF(message).writeByte(0).writeUTF("").toByteArray());
    }
}
