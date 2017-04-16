package com.transformice.bulle.rooms;

import com.transformice.bulle.rooms.threads.KillAllAFK;
import com.transformice.bulle.rooms.threads.MapStart;
import com.transformice.bulle.users.GameClient;
import com.transformice.bulle.users.GameManage;
import com.transformice.config.Config;
import com.transformice.helpers.network.Outgoing;
import com.transformice.logging.Logging;
import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.*;

public class Rooms {

    public THashMap<String, GameClient> players;

    private GameManage gameManage;

    public final String name;
    public final String community;
    public final String roomName;
    public String currentShamanName = "";
    public String currentSecondShamanName = "";
    public String mapName = "";
    public String mapXML = "";

    public int currentShamanLevel = 0;
    public int currentShamanBadge = 0;
    public int currentSecondShamanBadge = 0;
    public int currentSecondShamanLevel = 0;
    public int currentMap = 0;
    public int lastCodePartie = 0;
    public int mapCode = -1;
    public int mapYesVotes = 0;
    public int mapNoVotes = 0;
    public int mapPerma = -1;
    public int mapStatus = 0;
    public int currentSyncCode = -1;
    public int roundTime = 120;
    public int gameStartTime = 0;
    public int currentShamanCode = -1;
    public int currentSecondShamanCode = -1;
    public int currentShamanType = -1;
    public int currentSecondShamanType = -1;
    public int numCompleted = 0;
    public int FSnumCompleted = 0;
    public int SSnumCompleted = 0;
    public int EMapCode = 0;
    public int objectID = 0;
    public int tempTotemCount = -1;
    public int addTime = 0;
    public int cloudID = 0;
    public int companionBox = -1;
    public int musicMapStatus = 0;
    public int survivorMapStatus = 0;
    public int changeMapAttemps = 0;
    public int highestScore = 0;
    public int secondHighestScore = 0;

    public long gameStartTimeMillis = 0;

    public boolean noShamanSkills = false;
    public boolean isCurrentlyPlay = false;
    public boolean isDoubleMap = false;
    public boolean isNoShamanMap = false;
    public boolean countStats = true;
    public boolean never20secTimer = false;
    public boolean isVanilla = false;
    public boolean isEditeur = false;
    public boolean changed20secTimer = false;
    public boolean specificMap = false;
    public boolean noShaman = false;
    public boolean isTutorial = false;
    public boolean isTotemEditeur = false;
    public boolean autoRespawn = false;
    public boolean iceEnabled = false;
    public boolean noAutoScore = false;
    public boolean catchTheCheeseMap = false;
    public boolean isTribeHouse = false;
    public boolean isTribeHouseMap = false;
    public boolean isRacing = false;
    public boolean isMusic = false;
    public boolean isBootcamp = false;
    public boolean isBootcampP13 = false;
    public boolean isSurvivor = false;
    public boolean isSurvivorVamp = false;
    public boolean isDefilante = false;
    public boolean isNormRoom = false;
    public boolean canChangeMap = true;
    public boolean isFixedMap = false;
    public boolean is801Room = false;
    public boolean isVillage = false;
    public boolean isRacingP17 = false;

    public final Random random;

    public final int[] MapList = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 136, 137, 138, 139, 140, 141, 142, 143, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210};
    public final int[] noShamanMaps = new int[]{7, 8, 14, 22, 23, 28, 29, 54, 55, 57, 58, 59, 60, 61, 70, 77, 78, 87, 88, 92, 122, 123, 124, 125, 126, 1007, 888, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210};
    public int[] transformation = new int[]{200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210};
    public String[] anchors = new String[]{};
    public int[] lastHandymouse = new int[]{-1, -1};

    public TMap<Integer, Integer> currentShamanSkills = new THashMap();
    public TMap<Integer, Integer> currentSecondShamanSkills = new THashMap();

    private ScheduledExecutorService tasks = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    public ScheduledFuture changeMap;
    public ScheduledFuture startMap;
    public ScheduledFuture killAfk;

    public Rooms(String name, GameManage gameManage) {
        this.name = name;
        if (Config.transformice.debug) {
            Logging.print("New room: " + this.name, "info");
        }
        this.players = new THashMap();
        this.gameManage = gameManage;

        this.random = new Random();
        if (this.name.startsWith("*")) {
            this.community = "xx";
            this.roomName = this.name;
        } else {
            this.community = StringUtils.split(this.name, "-")[0].toLowerCase();
            this.roomName = StringUtils.split(this.name, "-")[1];
        }
        if (this.roomName.startsWith((char) 3 + "[Editeur] ")) {
            this.countStats = false;
            this.isEditeur = true;
            this.never20secTimer = true;
        } else if (this.roomName.startsWith((char) 3 + "[Tutorial] ")) {
            this.countStats = false;
            this.currentMap = 900;
            this.specificMap = true;
            this.noShaman = true;
            this.never20secTimer = true;
            this.isTutorial = true;
        } else if (this.roomName.startsWith((char) 3 + "[Totem] ")) {
            this.countStats = false;
            this.specificMap = true;
            this.currentMap = 444;
            this.isTotemEditeur = true;
            this.never20secTimer = true;
        } else if (this.roomName.startsWith("*" + (char) 3)) {
            this.countStats = false;
            this.isTribeHouse = true;
            this.autoRespawn = true;
            this.never20secTimer = true;
            this.noShaman = true;
        } else if (this.roomName.startsWith("music")) {
            this.isMusic = true;
        } else if (this.roomName.startsWith("racing") || this.roomName.startsWith("*racing")) {
            this.isRacing = true;
            this.noShaman = true;
            this.roundTime = 63;
        } else if (this.roomName.startsWith("bootcamp") || this.roomName.startsWith("*bootcamp")) {
            this.isBootcamp = true;
            this.countStats = false;
            this.roundTime = 360;
            this.never20secTimer = true;
            this.autoRespawn = true;
            this.noShaman = true;
        } else if (this.roomName.startsWith("vanilla")) {
            this.isVanilla = true;
        } else if (this.roomName.startsWith("survivor")) {
            this.isSurvivor = true;
            this.roundTime = 90;
        } else if (this.roomName.startsWith("defilante")) {
            this.isDefilante = true;
            this.noShaman = true;
            this.countStats = false;
            this.noAutoScore = true;
        } else if (this.roomName.startsWith("village")) {
            this.isVillage = true;
            this.roundTime = 0;
            this.never20secTimer = true;
            this.autoRespawn = true;
            this.countStats = false;
            this.noShaman = true;
            this.isFixedMap = true;
        } else if (this.roomName.equals("801") || this.roomName.equals("*801")) {
            this.is801Room = true;
            this.roundTime = 0;
            this.never20secTimer = true;
            this.autoRespawn = true;
            this.countStats = false;
            this.noShaman = true;
            this.isFixedMap = true;
        } else {
            this.isNormRoom = true;
        }
        this.startMap();

    }

    public ScheduledFuture<?> scheduleTask(Runnable task, long delay, TimeUnit tu) {
        return this.scheduleTask(task, delay, tu, false);
    }

    public ScheduledFuture<?> scheduleTask(Runnable task, long delay, TimeUnit tu, boolean repeat) {
        return repeat ? this.tasks.scheduleAtFixedRate(task, delay, delay, tu) : this.tasks.schedule(task, delay, tu);
    }

    public ScheduledFuture<?> scheduleTask(Runnable task, long start, long delay, TimeUnit tu, boolean repeat) {
        return repeat ? this.tasks.scheduleAtFixedRate(task, start, delay, tu) : this.tasks.schedule(task, delay, tu);
    }

    public int getTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public void enterPlayer(GameClient client) {
        if (!this.players.containsKey(client.playerID)) {
            this.players.put(client.username, client);
            client.room = this;
            client.isDead = this.isCurrentlyPlay;
            client.startPlay();
            this.sendAllOthersOld(client, Outgoing.player_respawn, client.getPlayerData());
            client.enterRoom(this.name);
        }
    }

    public void removePlayer(GameClient client) {
        if (this.players.containsKey(client.username)) {
            this.players.remove(client.username);
            client.sendPlayerDie();
            this.sendAllOld(Outgoing.player_disconnect, client.playerCode);
            this.getSyncCode();
            this.checkShouldChangeMap();
            if (this.players.size() <= 0) {
                this.gameManage.rooms.remove(this.name);
                this.tasks.shutdownNow();
            }
        }
    }

    public void startMap() {
        if (this.killAfk != null) this.killAfk.cancel(true);
        if (this.startMap != null) this.startMap.cancel(true);
        if (this.changeMap != null) this.changeMap.cancel(true);
        this.lastCodePartie = ++this.lastCodePartie % Byte.MAX_VALUE;
        int lastObjectID = this.objectID;

        this.currentSyncCode = -1;
        this.currentShamanCode = -1;
        this.currentSecondShamanCode = -1;
        this.currentShamanName = "";
        this.currentShamanLevel = 0;
        this.currentShamanBadge = 0;
        this.currentSecondShamanName = "";
        this.currentSecondShamanLevel = 0;
        this.currentSecondShamanBadge = 0;
        this.currentShamanType = -1;
        this.currentSecondShamanType = -1;
        this.currentShamanSkills.clear();
        this.currentSecondShamanSkills.clear();
        this.changed20secTimer = false;
        this.isDoubleMap = false;
        this.isNoShamanMap = false;
        this.FSnumCompleted = 0;
        this.SSnumCompleted = 0;
        this.iceEnabled = false;
        this.cloudID = 0;
        this.objectID = 0;
        this.tempTotemCount = -1;
        this.addTime = 0;
        this.companionBox = -1;
        this.lastHandymouse = new int[]{-1, -1};
        this.isTribeHouseMap = false;
        this.canChangeMap = true;
        this.changeMapAttemps = 0;

        this.getSyncCode();

        this.anchors = new String[]{};

        this.mapStatus = ++this.mapStatus % 13;
        this.musicMapStatus = ++this.musicMapStatus % 6;
        this.survivorMapStatus = ++this.survivorMapStatus % 8;

        this.isBootcampP13 = !this.isBootcampP13;

        this.numCompleted = 0;
        this.isRacingP17 = !this.isRacingP17;

        this.currentMap = this.selectMap();

        if ((ArrayUtils.contains(new int[]{44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 138, 139, 140, 141, 142, 143}, this.currentMap) || this.mapPerma == 8) && this.players.size() >= 2) {
            this.isDoubleMap = true;
        }

        if (this.mapPerma == 7 || this.isSurvivorVamp) {
            this.isNoShamanMap = true;
        }

        if (ArrayUtils.contains(new int[]{108, 109, 110, 111, 112, 113}, this.currentMap)) {
            this.catchTheCheeseMap = true;
        }

        this.gameStartTime = this.getTime();
        this.gameStartTimeMillis = System.currentTimeMillis();
        this.isCurrentlyPlay = false;

        for (GameClient player : this.players.values()) {
            player.resetPlay();
        }

        for (GameClient player : this.players.values()) {
            player.startPlay();
        }
        this.roundTime = this.roundTime + this.addTime;
        this.highestScore = this.getHighestScore();
        this.secondHighestScore = this.getSecondHighestScore();

        this.killAfk = this.scheduleTask(new KillAllAFK(this), 20, TimeUnit.SECONDS);
        this.startMap = this.scheduleTask(new MapStart(this), 3, TimeUnit.SECONDS);
        this.changeMap = this.scheduleTask(() -> this.startMap(), this.roundTime, TimeUnit.SECONDS);
    }

    public int getHighestScore() {
        if (this.players.size() >= 1) {
            List<GameClient> scores = new ArrayList(this.players.values());
            GameClient client = Collections.max(scores, (player1, player2) -> player1.playerScore > player2.playerScore ? 1 : -1);
            return client.playerCode;
        }
        return 0;
    }

    public int getSecondHighestScore() {
        List<GameClient> scores = new ArrayList(this.players.values());
        if (scores.size() >= 2) {
            GameClient highScore = Collections.max(scores, (player1, player2) -> player1.playerScore > player2.playerScore ? 1 : -1);
            scores.remove(highScore);
            GameClient client = Collections.max(scores, (player1, player2) -> player1.playerScore > player2.playerScore ? 1 : -1);
            return client.playerCode;
        }
        return 0;
    }

    public int getSyncCode() {
        if (this.players.size() > 0) {
            if (this.currentSyncCode == -1) {
                List<GameClient> clients = new ArrayList(this.players.values());
                GameClient client = Collections.max(clients, (player1, player2) -> player1.pingTime < player2.pingTime ? 1 : -1);
                this.currentSyncCode = client.playerCode;
            }
        } else {
            if (this.currentSyncCode == -1) {
                this.currentSyncCode = 0;
            }
        }
        return this.currentSyncCode;
    }

    public int selectMap() {
        if (this.isEditeur) {
            return this.EMapCode;
        } else if (this.isTribeHouse) {
            String tribeName = this.roomName.substring(2);
            int runMap = 0;//this.server.getTribeHouse(tribeName);
            if (runMap == 0) {
                this.mapCode = 0;
                this.mapName = "Tigrounette";
                this.mapXML = "<C><P /><Z><S><S Y=\"360\" T=\"0\" P=\"0,0,0.3,0.2,0,0,0,0\" L=\"800\" H=\"80\" X=\"400\" /></S><D><P Y=\"0\" T=\"34\" P=\"0,0\" X=\"0\" C=\"719b9f\" /><T Y=\"320\" X=\"49\" /><P Y=\"320\" T=\"16\" X=\"224\" P=\"0,0\" /><P Y=\"319\" T=\"17\" X=\"311\" P=\"0,0\" /><P Y=\"284\" T=\"18\" P=\"1,0\" X=\"337\" C=\"57703e,e7c3d6\" /><P Y=\"284\" T=\"21\" X=\"294\" P=\"0,0\" /><P Y=\"134\" T=\"23\" X=\"135\" P=\"0,0\" /><P Y=\"320\" T=\"24\" P=\"0,1\" X=\"677\" C=\"46788e\" /><P Y=\"320\" T=\"26\" X=\"588\" P=\"1,0\" /><P Y=\"193\" T=\"14\" P=\"0,0\" X=\"562\" C=\"95311e,bde8f3,faf1b3\" /></D><O /></Z></C>";
                this.mapYesVotes = 0;
                this.mapNoVotes = 0;
                this.mapPerma = 22;
            } else {
                this.mapCode = 0;
                this.mapName = "Tigrounette";
                this.mapXML = "<C><P /><Z><S><S Y=\"360\" T=\"0\" P=\"0,0,0.3,0.2,0,0,0,0\" L=\"800\" H=\"80\" X=\"400\" /></S><D><P Y=\"0\" T=\"34\" P=\"0,0\" X=\"0\" C=\"719b9f\" /><T Y=\"320\" X=\"49\" /><P Y=\"320\" T=\"16\" X=\"224\" P=\"0,0\" /><P Y=\"319\" T=\"17\" X=\"311\" P=\"0,0\" /><P Y=\"284\" T=\"18\" P=\"1,0\" X=\"337\" C=\"57703e,e7c3d6\" /><P Y=\"284\" T=\"21\" X=\"294\" P=\"0,0\" /><P Y=\"134\" T=\"23\" X=\"135\" P=\"0,0\" /><P Y=\"320\" T=\"24\" P=\"0,1\" X=\"677\" C=\"46788e\" /><P Y=\"320\" T=\"26\" X=\"588\" P=\"1,0\" /><P Y=\"193\" T=\"14\" P=\"0,0\" X=\"562\" C=\"95311e,bde8f3,faf1b3\" /></D><O /></Z></C>";
                this.mapYesVotes = 0;
                this.mapNoVotes = 0;
                this.mapPerma = 22;
            }
        } else if (this.isVanilla) {
            this.mapCode = -1;
            this.mapName = "Invalid";
            this.mapXML = "<C><P /><Z><S /><D /><O /></Z></C>";
            this.mapYesVotes = 0;
            this.mapNoVotes = 0;
            this.mapPerma = -1;
            int map = this.MapList[ThreadLocalRandom.current().nextInt(this.MapList.length)];
            while (map == this.currentMap) {
                map = this.MapList[ThreadLocalRandom.current().nextInt(this.MapList.length)];
            }
            return map;
        } else {
            this.mapCode = -1;
            this.mapName = "Invalid";
            this.mapXML = "<C><P /><Z><S /><D /><O /></Z></C>";
            this.mapYesVotes = 0;
            this.mapNoVotes = 0;
            this.mapPerma = -1;
            return this.selectMapStatus(this.mapStatus);
        }
        return -1;
    }

    private int selectMapStatus(final int mapStatus) {
        return this.MapList[ThreadLocalRandom.current().nextInt(this.MapList.length)];
    }

    public void sendAll(int[] tokens, byte... packet) {
        for (GameClient client : this.players.values()) {
            client.sendPacket(tokens, packet);
        }
    }

    public void sendAll(int[] tokens, int... packet) {
        for (GameClient client : this.players.values()) {
            client.sendPacket(tokens, packet);
        }
    }

    public void sendAllOld(int[] tokens, Object... packet) {
        for (GameClient client : this.players.values()) {
            client.sendOldPacket(tokens, packet);
        }
    }

    public void sendAllOthers(GameClient senderPlayer, int[] tokens, byte... packet) {
        for (GameClient client : this.players.values()) {
            if (!client.equals(senderPlayer)) {
                client.sendPacket(tokens, packet);
            }
        }
    }

    public void sendAllOthersOld(GameClient senderPlayer, int[] tokens, Object... packet) {
        for (GameClient client : this.players.values()) {
            if (!client.equals(senderPlayer)) {
                client.sendOldPacket(tokens, packet);
            }
        }
    }

    public Object[] getPlayerList() {
        List<String> result = new ArrayList(this.players.size());
        for (GameClient client : this.players.values()) {
            result.add(client.getPlayerData());
        }
        return result.toArray();
    }

    public boolean checkIfShamanIsDead() {
        GameClient client = this.players.get(this.currentShamanName);
        return client == null ? false : client.isDead;
    }

    public boolean checkIfDoubleShamansAreDead() {
        GameClient client1 = this.players.get(this.currentShamanName);
        GameClient client2 = this.players.get(this.currentSecondShamanName);
        return (client1 == null ? false : client1.isDead) && (client2 == null ? false : client2.isDead);
    }

    public boolean checkIfTooFewRemaining() {
        return this.players.values().stream().filter(client -> !client.isDead).count() <= 2;
    }

    public int[] checkDeathCount() {
        int[] counts = new int[]{0, 0};
        for (GameClient client : this.players.values()) {
            counts[client.isDead ? 0 : 1]++;
        }
        return counts;
    }

    public void send20SecRemainingTimer() {
        if (!this.changed20secTimer) {
            this.changed20secTimer = true;
            int calc = this.roundTime + (this.gameStartTime - this.getTime());

            if (this.never20secTimer || calc < 21 || this.roundTime == 0 || this.isEditeur) {
            } else {
                this.sendAllOld(Outgoing._20_seconds);
                this.changeMapTimer(20);
            }
        }
    }

    public void checkShouldChangeMap() {
        if (!this.isBootcamp || !this.autoRespawn || !(this.isTribeHouse && this.isTribeHouseMap) || !this.isFixedMap) {
            if (this.players.values().stream().allMatch(client -> client.isDead)) {
                this.startMap();
            }
        }
    }

    public void changeMapTimer(int timer) {
        for (GameClient client : this.players.values()) {
            client.sendRoundTime(timer);
        }
        if (this.changeMap != null) this.changeMap.cancel(true);
        this.changeMap = this.scheduleTask(() -> this.startMap(), timer, TimeUnit.SECONDS, true);
    }
}