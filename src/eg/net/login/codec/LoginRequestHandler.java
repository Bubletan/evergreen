package eg.net.login.codec;

import eg.model.player.Player;
import eg.net.game.GameSession;
import eg.net.game.codec.GamePacketDecoder;
import eg.net.game.codec.GamePacketEncoder;
import eg.net.game.codec.GameProtocolDecoder;
import eg.net.game.codec.GameProtocolEncoder;
import eg.net.login.LoginRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;

@Sharable
public final class LoginRequestHandler extends ChannelInboundHandlerAdapter {
	
	private static final GamePacketDecoder GAME_PACKET_DECODER = new GamePacketDecoder();
	private static final GamePacketEncoder GAME_PACKET_ENCODER = new GamePacketEncoder();
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		LoginRequest request = (LoginRequest) msg;
		
		ByteBuf buf = ctx.alloc().buffer(3);
		buf.writeByte(2).writeByte(2).writeByte(0);
		ctx.channel().writeAndFlush(buf);
		
		ChannelPipeline p = ctx.pipeline();
		
		p.remove("loginProtocolDecoder");
		p.remove("loginRequestHandler");
		
		p.addLast("gameProtocolDecoder", new GameProtocolDecoder(request.getDecodingCipher()));
		p.addLast("gamePacketDecoder", GAME_PACKET_DECODER);
		GameSession session = new GameSession(ctx.channel());
		Player player = new Player(session, request.getUsername(), request.getPassword());
		p.addLast("gamePacketInboundHandler", session);
		
		p.addLast("gameProtocolEncoder", new GameProtocolEncoder(request.getEncodingCipher()));
		p.addLast("gamePacketEncoder", GAME_PACKET_ENCODER);
		
		player.setActive(true);
	}
}
