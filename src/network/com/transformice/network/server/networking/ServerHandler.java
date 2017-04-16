package com.transformice.network.server.networking;

import com.transformice.server.Server;
import com.transformice.helpers.network.ByteArray;
import com.transformice.server.users.GameClient;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.Channel;

public class ServerHandler extends SimpleChannelHandler {
    private final Server server;

    public ServerHandler(Server server) {
        this.server = server;
    }

    @Override
    public void channelOpen(ChannelHandlerContext context, ChannelStateEvent e) {
        this.server.gameManage.addClient(context.getChannel());
    }

    @Override
    public void channelClosed(ChannelHandlerContext context, ChannelStateEvent e) {
        this.server.gameManage.removeClient(context.getChannel());
    }

    @Override
    public void messageReceived(ChannelHandlerContext context, MessageEvent e) {
        if ((e.getMessage() instanceof byte[])) {
            byte[] buff = (byte[]) e.getMessage();
            ByteArray packet = new ByteArray(buff);
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
        this.server.packetManage.handlePacket((GameClient) channel.getAttachment(), packet, packetID);
    }
}