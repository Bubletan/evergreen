package eg.net.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public final class GameSession extends ChannelInboundHandlerAdapter {
	
	private final Channel channel;
	
	private final Queue<AbstractGamePacket> queue = new LinkedList<>();
	
	public GameSession(Channel channel) {
		this.channel = channel;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		synchronized (queue) {
			queue.add((AbstractGamePacket) msg);
		}
	}
	
	public List<AbstractGamePacket> receive() {
		synchronized (queue) {
			List<AbstractGamePacket> list = new ArrayList<>(queue);
			queue.clear();
			return list;
		}
	}
	
	public void send(AbstractGamePacket packet) {
		channel.writeAndFlush(packet);
	}
	
	public String getIP() {
		return channel.remoteAddress().toString().split(":")[0].replace("/", "");
	}
}
