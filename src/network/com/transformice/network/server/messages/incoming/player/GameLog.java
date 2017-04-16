package com.transformice.network.server.messages.incoming.player;

import com.transformice.helpers.Tokens;
import com.transformice.helpers.network.Incoming;
import com.transformice.logging.Logging;
import com.transformice.network.server.messages.incoming.MessageHandler;

@Tokens(C = Incoming._28.C, CC = Incoming._28.game_log)
public class GameLog extends MessageHandler {

    @Override
    public void handle() {
        if (this.client.logged) {
            byte error_c = this.packet.readByte();
            byte error_cc = this.packet.readByte();
            int error_old_c = this.packet.readUnsignedByte();
            int error_old_cc = this.packet.readUnsignedByte();
            String message = this.packet.readUTF();
            if (error_c == 1 && error_cc == 1) {
                Logging.packet(new StringBuilder().append("Warning: [").append(this.client.username).append("] [Old Protocol] GameLog Error: C: ").append(error_old_c).append(" CC: ").append(error_old_cc).append(" Error: ").append(message).toString());
            } else if (error_c == 60 && error_cc == 1) {
                Logging.packet(new StringBuilder("Warning: [").append(this.client.username).append("] [Tribulle] GameLog Error: Code: ").append(error_old_c).append(" Error: ").append(message).toString());
            } else {
                Logging.packet(new StringBuilder("Warning: [").append(this.client.username).append("] GameLog Error: C: ").append(error_c).append(" CC: ").append(error_cc).append(" Error: ").append(message).toString());
            }
        }
    }
}
