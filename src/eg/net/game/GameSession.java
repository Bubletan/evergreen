package eg.net.game;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class GameSession extends ChannelInboundHandlerAdapter {
    
    private final Channel channel;
    private ChannelFuture future;
    
    private final List<AbstractGamePacket> queue = new ArrayList<>();
    
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
        if (channel.isActive() && channel.isOpen()) {
            future = channel.writeAndFlush(packet);
        }
    }
    
    public void close() {
        if (future != null) {
            future.addListener(ChannelFutureListener.CLOSE);
            future = null;
        } else {
            channel.close();
        }
    }
    
    public String getIP() {
        return channel.remoteAddress().toString().split(":")[0].replace("/", "");
    }
}
