package com.transformice.helpers.network.codec;

import java.nio.charset.Charset;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class MessageEncoder extends SimpleChannelHandler {
    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) {
        if (e.getMessage() instanceof String) {
            Channels.write(ctx, e.getFuture(), ChannelBuffers.copiedBuffer((String) e.getMessage(), Charset.forName("UTF-8")));
        } else {
            Channels.write(ctx, e.getFuture(), e.getMessage());
        }
    }
}