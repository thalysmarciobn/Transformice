package com.transformice.network.bulle.networking;

import com.transformice.bulle.Bulle;
import com.transformice.config.Config;
import com.transformice.helpers.network.codec.MessageDecoder;
import com.transformice.helpers.network.codec.MessageEncoder;
import com.transformice.logging.Logging;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.oio.OioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameServer {
    private String host;

    private List<Integer> ports;

    public List<Integer> validPorts;

    private final Bulle bulle;

    private final ServerBootstrap bootstrap;
    private final ChannelPipeline pipeline;

    public GameServer(Bulle bulle, String host, List<Integer> ports) {
        this.bulle = bulle;
        this.host = host;
        this.ports = ports;
        this.validPorts = new ArrayList();

        this.bootstrap = new ServerBootstrap(new OioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        this.pipeline = this.bootstrap.getPipeline();
    }

    public void initialise() {
        this.pipeline.addLast("messageDecoder", new MessageDecoder());
        this.pipeline.addLast("messageEncoder", new MessageEncoder());
        this.pipeline.addLast("messageHandler", new ServerHandler(this.bulle));
        this.pipeline.addLast("pipelineExecutor", new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(Config.netty.corePoolSize, Config.netty.maxChannelMemorySize, Config.netty.maxTotalMemorySize, Config.netty.keepAliveTime, TimeUnit.MILLISECONDS, Executors.defaultThreadFactory())));

    }

    public void bind() {
        for (int port : this.ports) {
            Channel channel = this.bootstrap.bind(new InetSocketAddress(this.host, port));
            if (!channel.isBound()) {
                Logging.print("Failed to listen port: " + port, "info");
            } else {
                this.validPorts.add(port);
            }
        }
    }
}