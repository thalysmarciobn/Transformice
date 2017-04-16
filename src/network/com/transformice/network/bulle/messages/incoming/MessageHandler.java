package com.transformice.network.bulle.messages.incoming;

import com.transformice.bulle.Bulle;
import com.transformice.network.bulle.messages.PacketManage;
import com.transformice.bulle.users.GameClient;
import com.transformice.helpers.network.ByteArray;

import java.rmi.RemoteException;

public abstract class MessageHandler {

    public Bulle bulle;
    public GameClient client;
    public ByteArray packet;
    public int packetID;
    public PacketManage packetManage;

    public abstract void handle() throws RemoteException;
}