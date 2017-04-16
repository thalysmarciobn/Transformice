package com.transformice.network.bulle.networking;

import com.transformice.bulle.Bulle;
import com.transformice.bulle.users.GameClient;
import com.transformice.helpers.network.ByteArray;
import org.jboss.netty.channel.*;

public class ServerHandler extends SimpleChannelHandler {
    private final Bulle bulle;

    public ServerHandler(Bulle bulle) {
        this.bulle = bulle;
    }

    @Override
    public void channelOpen(ChannelHandlerContext context, ChannelStateEvent e) {
        if (!this.bulle.sessionManager.addClient(context.getChannel())) {
            context.getChannel().close();
        }
    }

    @Override
    public void channelClosed(ChannelHandlerContext context, ChannelStateEvent e) {
        if (this.bulle.sessionManager.containsClient(context.getChannel())) {
            this.bulle.sessionManager.removeClient(context.getChannel());
        }
        context.getChannel().close();
    }

    @Override
    public void messageReceived(ChannelHandlerContext context, MessageEvent e) {
        if ((e.getMessage() instanceof byte[])) {
            ByteArray packet = new ByteArray((byte[]) e.getMessage());
            if (packet.size() > 2) {
                byte sizeBytes = packet.readByte();
                int length = sizeBytes == 1 ? packet.readUnsignedByte() : sizeBytes == 2 ? packet.readUnsignedShort() : sizeBytes == 3 ? ((packet.readUnsignedByte() & 0xFF) << 16) | ((packet.readUnsignedByte() & 0xFF) << 8) | (packet.readUnsignedByte() & 0xFF) : 0;
                if (length != 0) {
                    byte packetID = packet.readByte();
                    if (packet.size() == length) {
                        if (packet.size() >= 2) {
                            this.parse(context.getChannel(), packet, packetID);
                        }
                    } else if (packet.size() > length) {
                        byte[] data = packet.read(new byte[length]);
                        if (length >= 2) {
                            this.parse(context.getChannel(), new ByteArray(data), packetID);
                        }
                    }
                }
            }
        }
    }

    public void parse(Channel channel, ByteArray packet, int packetID) {
        this.bulle.packetManage.handlePacket((GameClient) channel.getAttachment(), packet, packetID);
    }
}