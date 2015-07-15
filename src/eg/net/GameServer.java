package eg.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import eg.Config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class GameServer {
    
    private static final ChannelInboundHandler SESSION_HANDLER = new SessionHandler();
    
    private final ServerBootstrap bootstrap = new ServerBootstrap();
    private final EventLoopGroup group = new NioEventLoopGroup();
    
    public GameServer() {
        bootstrap.group(group);
        bootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                
                pipeline.addLast("handshakeHandler", new HandshakeHandler());
                
                pipeline.addFirst("sessionHandler", SESSION_HANDLER);
                pipeline.addFirst("idleStateHandler", new IdleStateHandler(Config.IDLE_TIME_SECONDS, 0, 0));
            }
        });
    }
    
    public void bind(int port) {
        try (Socket socket = new Socket("localhost", port)) {
            throw new IllegalStateException("Port unavailable: " + port);
        } catch (IOException e) {
        }
        bootstrap.bind(new InetSocketAddress(port));
    }
}
