package eg.net.game;

import java.util.List;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

@Sharable
public final class GamePacketEncoder extends MessageToMessageEncoder<AbstractGamePacket> {
    
    private final GameProtocol protocol;
    
    public GamePacketEncoder(GameProtocol protocol) {
        this.protocol = protocol;
    }
    
    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractGamePacket in, List<Object> out) throws Exception {
        encode(in.getClass(), in, out);
    }
    
    @SuppressWarnings("unchecked")
    private <T extends AbstractGamePacket> void encode(Class<T> type, AbstractGamePacket in, List<Object> out) {
        AbstractGamePacketEncoder<T> encoder = protocol.getOutboundPacketEncoder(type);
        if (encoder != null) {
            try {
                out.add(encoder.encode((T) in));
            } catch (Exception e) {
                System.err.println("Error encoding packet: " + type.getSimpleName());
                e.printStackTrace();
            }
        } else {
            System.err.println("Unsupported outbound packet type: " + type.getSimpleName());
        }
    }
}
