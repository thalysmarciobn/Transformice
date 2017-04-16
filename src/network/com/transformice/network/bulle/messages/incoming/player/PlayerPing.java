package com.transformice.network.bulle.messages.incoming.player;

import com.transformice.helpers.network.Incoming;
import com.transformice.network.bulle.messages.incoming.MessageHandler;
import com.transformice.helpers.Tokens;
import jdbchelper.QueryResult;

import java.rmi.RemoteException;

@Tokens(C = Incoming._28.C, CC = Incoming._28.ping)
public class PlayerPing extends MessageHandler {

    @Override
    public void handle() throws RemoteException {
        if (this.client.logged) {
            //long pingTimeMillis = this.bulle.rmi.getPing(this.client.playerCode);
            //this.client.pingTime = System.currentTimeMillis() - pingTimeMillis;
        }
    }
}