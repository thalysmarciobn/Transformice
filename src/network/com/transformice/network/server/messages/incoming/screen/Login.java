package com.transformice.network.server.messages.incoming.screen;

import com.transformice.helpers.network.Incoming;
import com.transformice.logging.Logging;
import com.transformice.network.server.messages.incoming.MessageHandler;
import com.transformice.helpers.Tokens;
import com.transformice.helpers.network.Outgoing;
import com.transformice.config.Config;
import jdbchelper.QueryResult;

@Tokens(C = Incoming._26.C, CC = Incoming._26.login)
public class Login extends MessageHandler {

    @Override
    public void handle() {
        this.packet = this.packetManage.decrypt(this.packetID, this.packet, Config.transformice.packetKeys);
        String playerName = this.client.gameManage.parsePlayerName(this.packet.readUTF());
        String password = this.packet.readUTF();
        this.client.link = this.packet.readUTF();
        String startRoom = this.packet.readUTF();
        int resultKey = this.packet.readInt();
        int authKey = this.client.authKey;
        for (int key : Config.transformice.loginKeys) {
            authKey ^= key;
        }
        if (!playerName.matches("^[A-Za-z][A-Za-z0-9_]{2,11}$") || playerName.length() > 25 || (playerName.length() >= 1 && playerName.substring(1).contains("+"))) {
            this.client.dispose();
        } else if (authKey == resultKey && this.client.username == null) {
            playerName = playerName.equals("") ? "Souris" : playerName;
            if (this.client.gameManage.playersLogged.contains(playerName)) {
                this.client.sendPacket(Outgoing.login_result, 1);
            } else {
                if (!password.equals("")) {
                    try {
                        QueryResult result = this.server.database.query("SELECT * FROM users WHERE Username = ? AND Password = ?", playerName, password);
                        if (result.next()) {
                            this.client.playerID = result.getInt("id");
                            this.client.username = result.getString("Username");
                            this.client.privilegeLevel = result.getInt("Privilege");
                            this.client.look = result.getString("Look");
                            this.client.shamanLook = result.getString("ShamanLook");
                            this.client.color = result.getString("Color");
                            this.client.shamanColor = result.getString("ShamanColor");
                            this.client.isGuest = false;
                            this.client.logged = true;
                            this.client.enterPlayer(startRoom);
                        } else {
                            this.client.sendPacket(Outgoing.login_result, 2);
                        }
                        result.close();
                    } catch (Exception e) {
                        Logging.error(e);
                        this.client.sendPacket(Outgoing.login_result, 6);
                    }
                } else {
                    this.client.playerID = 0;
                    this.client.username = "Souris";
                    this.client.privilegeLevel = 0;
                    this.client.look = "1;0,0,0,0,0,0,0,0,0";
                    this.client.shamanLook = "0,0,0,0,0,0,0,0,0,0";
                    this.client.color = "78583a";
                    this.client.shamanColor = "95d9d6";
                    this.client.isGuest = true;
                    this.client.logged = true;
                    this.client.enterPlayer(startRoom);
                }
            }
        }
    }
}