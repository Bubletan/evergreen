package eg.util.net;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public abstract class TransientByteToMessageDecoder extends ByteToMessageDecoder {
    
    @Override
    protected void callDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        super.callDecode(ctx, in, out);
        if (in.isReadable() && ctx.isRemoved()) {
            out.add(in.readBytes(in.readableBytes()));
        }
    }
    
    @Override
    protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        super.decodeLast(ctx, in, out);
        if (in.isReadable() && ctx.isRemoved()) {
            out.add(in.readBytes(in.readableBytes()));
        }
    }
}
