package eg.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import eg.Config;
import eg.net.login.codec.LoginProtocolDecoder;
import eg.net.login.codec.LoginRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public final class GameServer {
	
	private static final ChannelInboundHandler LOGIN_REQUEST_HANDLER = new LoginRequestHandler();
	private static final ChannelInboundHandler GAME_SESSION_HANDLER;
	static {
		@Sharable
		class GameSessionHandler extends ChannelInboundHandlerAdapter {
			@Override
			public void channelInactive(ChannelHandlerContext ctx) {
				ctx.channel().close();
			}
			@Override
			public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
				ctx.channel().close();
			}
		}
		GAME_SESSION_HANDLER = new GameSessionHandler();
	}

	
	private final ServerBootstrap bootstrap = new ServerBootstrap();
	private final EventLoopGroup group = new NioEventLoopGroup();
	
	public GameServer() {
		bootstrap.group(group);
		bootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast("loginProtocolDecoder", new LoginProtocolDecoder());
				p.addLast("loginRequestHandler", LOGIN_REQUEST_HANDLER);
				p.addFirst("gameSessionHandler", GAME_SESSION_HANDLER);
				p.addFirst("idleStateHandler", new IdleStateHandler(Config.IDLE_TIME_SECONDS, 0, 0));
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
