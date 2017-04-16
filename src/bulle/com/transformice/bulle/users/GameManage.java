package com.transformice.bulle.users;

import com.transformice.bulle.Bulle;
import com.transformice.bulle.rooms.Rooms;
import com.transformice.bulle.users.commands.CommandManage;
import gnu.trove.map.hash.THashMap;
import org.jboss.netty.channel.Channel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameManage {

    private final THashMap<Integer, GameClient> clients;
    public final THashMap<String, Rooms> rooms;
    public final Bulle bulle;
    public final CommandManage commandManage;

    public GameManage(Bulle bulle) throws Exception {
        this.clients = new THashMap();
        this.rooms = new THashMap();
        this.bulle = bulle;
        this.commandManage = new CommandManage();
    }

    public boolean containsClient(Channel channel) {
        return this.clients.containsKey(channel.getId());
    }

    public boolean addClient(Channel channel) {
        GameClient client = new GameClient(channel, this);
        channel.setAttachment(client);
        return this.clients.putIfAbsent(channel.getId(), client) == null;
    }

    public GameClient getClient(Channel channel) {
        if (this.clients.containsKey(channel.getId())) {
            return (GameClient) channel.getAttachment();
        }
        return null;
    }

    public void removeClient(Channel channel) {
        GameClient client = this.getClient(channel);
        if (client != null) {
            client.dispose();
        }
        this.clients.remove(channel.getId());
    }

    public void enterRoom(String roomName, GameClient client) {
        if (!this.rooms.containsKey(roomName)) {
            this.rooms.put(roomName, new Rooms(roomName, this));
        }
        this.rooms.get(roomName).enterPlayer(client);
    }
}
