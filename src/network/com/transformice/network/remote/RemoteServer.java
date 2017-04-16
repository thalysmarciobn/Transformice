package com.transformice.network.remote;

import com.transformice.config.Config;
import com.transformice.helpers.network.codec.MessageDecoder;
import com.transformice.helpers.network.codec.MessageEncoder;
import com.transformice.network.server.networking.ServerHandler;
import com.transformice.server.Server;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.oio.OioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RemoteServer {

    private final Server server;
    private final String host;
    private final int port;

    private final ServerBootstrap bootstrap;
    private final ChannelPipeline pipeline;

    public RemoteServer(Server server, String host, int port) {
        this.server = server;
        this.host = host;
        this.port = port;

        this.bootstrap = new ServerBootstrap(new OioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        this.pipeline = this.bootstrap.getPipeline();
    }

    public void initialise() {
        this.pipeline.addLast("messageHandler", new ServerHandler(this.server));
        this.pipeline.addLast("pipelineExecutor", new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(Config.netty.corePoolSize, Config.netty.maxChannelMemorySize, Config.netty.maxTotalMemorySize, Config.netty.keepAliveTime, TimeUnit.MILLISECONDS, Executors.defaultThreadFactory())));
    }
}
