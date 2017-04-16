package com.transformice.server.users;

import com.transformice.server.users.commands.CommandManage;
import com.transformice.server.Server;
import com.transformice.server.rooms.Rooms;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.THashMap;
import gnu.trove.stack.array.TIntArrayStack;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

public class GameManage {

    public final THashMap<String, Rooms> rooms;
    public final THashMap<Integer, GameClient> players;
    public final List<String> playersLogged;
    public final TIntArrayList connections;
    public final Server server;
    public final CommandManage commandManage;

    public int playerCode = 0;

    public GameManage(Server server) throws Exception {
        this.rooms = new THashMap();
        this.players = new THashMap();
        this.playersLogged = new ArrayList();
        this.connections = new TIntArrayList();
        this.server = server;
        this.commandManage = new CommandManage();
    }

    public void addClient(Channel channel) {
        GameClient client = new GameClient(channel, this);
        channel.setAttachment(client);
        this.connections.add(channel.getId());
    }

    public GameClient getClient(Channel channel) {
        return (GameClient) channel.getAttachment();
    }

    public GameClient getPlayer(int playerCode) {
        if (this.players.containsKey(playerCode)) {
            return this.players.get(playerCode);
        }
        return null;
    }

    public void removeClient(Channel channel) {
        GameClient client = this.getClient(channel);
        if (client != null) {
            client.dispose();
        }
        channel.setAttachment(null);
        this.connections.remove(channel.getId());
    }

    public String parsePlayerName(String playerName) {
        return playerName.startsWith("*") ? "*" + StringUtils.capitalize(playerName.substring(1).toLowerCase()) : StringUtils.capitalize(playerName.toLowerCase());
    }

    public String getRandom(int size) {
        return RandomStringUtils.random(size, "ABCDEFGHJKLMNOPQRSTUVXZ");
    }

    public void enterRoom(String roomName, GameClient client) {
        if (!this.rooms.containsKey(roomName)) {
            this.rooms.put(roomName, new Rooms(roomName, this));
        }
        this.rooms.get(roomName).enterPlayer(client);
    }
}
