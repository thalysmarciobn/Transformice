package com.transformice.network.server.messages.incoming;

import com.transformice.server.Server;
import com.transformice.network.server.messages.PacketManage;
import com.transformice.helpers.network.ByteArray;
import com.transformice.server.users.GameClient;

public abstract class MessageHandler {

    public Server server;
    public GameClient client;
    public ByteArray packet;
    public int packetID;
    public PacketManage packetManage;

    public abstract void handle();
}
