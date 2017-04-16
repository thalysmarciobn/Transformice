package com.transformice.helpers.network.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class MessageDecoder extends FrameDecoder {
    @Override
    protected Object decode(ChannelHandlerContext context, Channel channel, ChannelBuffer buffer) throws Exception {
        byte[] buff = new byte[buffer.readableBytes()];
        buffer.readBytes(buff);
        if (buff != null && buff.length > 2) {
            if (new String(buff).startsWith("<policy-file-request/>")) {
                buffer.discardReadBytes();
                channel.write("<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy>").addListener(ChannelFutureListener.CLOSE);
                return null;
            }
        }
        return buff;
    }
}
