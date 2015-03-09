package eg.net;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public final class SessionHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ctx.channel().close();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.channel().close();
    }
}
