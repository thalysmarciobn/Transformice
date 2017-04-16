package com.transformice.network.bulle.messages.incoming.player;

import com.transformice.helpers.network.Incoming;
import com.transformice.network.bulle.messages.incoming.MessageHandler;
import com.transformice.helpers.Tokens;
import jdbchelper.QueryResult;

import java.rmi.RemoteException;

@Tokens(C = Incoming._44.C, CC = Incoming._44.bulleID)
public class BulleID extends MessageHandler {

    @Override
    public void handle() throws RemoteException {
        if (!this.client.logged) {
            this.client.playerCode = this.packet.readInt();
            //if (this.bulle.rmi.containsPlayer(this.client.playerCode)) {
            //    if (!this.bulle.rmi.getIfGuest(this.client.playerCode)) {
            //        this.client.playerID = this.bulle.rmi.getPlayerID(this.client.playerCode);
                    QueryResult result = this.bulle.database.query("SELECT * FROM users WHERE id = ?", this.client.playerID);
                    if (result.next()) {
                        this.client.username = result.getString("Username");
                        this.client.privilegeLevel = result.getInt("Privilege");
                        this.client.langue = result.getString("CountryCode");
                        this.client.look = result.getString("Look");
                        this.client.shamanLook = result.getString("ShamanLook");
                        this.client.color = result.getString("Color");
                        this.client.shamanColor = result.getString("ShamanColor");
                        this.client.title = result.getString("Title");
            //            this.client.gameManage.enterRoom(this.bulle.rmi.getRoom(this.client.playerCode), this.client);
                        this.client.logged = true;
                    } else {
                        this.client.dispose();
                    //    this.bulle.rmi.kickPlayer(this.client.playerCode);
                    }
                } else {
                    this.client.username = "Souris";
                    this.client.privilegeLevel = 0;
                    //this.client.langue = this.bulle.rmi.getLangue(this.client.playerCode);
                    this.client.look = "1;0,0,0,0,0,0,0,0,0";
                    this.client.shamanLook = "0,0,0,0,0,0,0,0,0,0";
                    this.client.color = "78583a";
                    this.client.shamanColor = "95d9d6";
                    this.client.title = "0.1";
                    //this.client.gameManage.enterRoom(this.bulle.rmi.getRoom(this.client.playerCode), this.client);
                    //this.client.logged = true;
                }
            //} else {
                this.client.dispose();
                //this.bulle.rmi.kickPlayer(this.client.playerCode);
            //}
        //}
    }
}