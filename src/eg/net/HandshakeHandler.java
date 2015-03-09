package eg.net;

import java.util.List;

import eg.net.login.codec.LoginHandler;
import eg.util.net.TransientByteToMessageDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;

public final class HandshakeHandler extends TransientByteToMessageDecoder {
    
    private static final int LOGIN = 14;
    
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
            List<Object> out) throws Exception {
        
        if (!in.isReadable()) { // TODO is this required?
            return;
        }
        
        ChannelPipeline pipeline = ctx.pipeline();
        
        int code = in.readUnsignedByte();
        switch (code) {
        
        case LOGIN:
            pipeline.addLast("loginHandler", new LoginHandler());
            break;
        
        default:
            throw new IllegalArgumentException("Unknown connection type code: " + code);
        }
        
        pipeline.remove(this);
    }
}
