package eg.net.game;

import java.util.List;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
@Sharable
public final class GamePacketDecoder extends MessageToMessageDecoder<GamePacket> {
    
    private final GameProtocol protocol;
    
    public GamePacketDecoder(GameProtocol protocol) {
        this.protocol = protocol;
    }
    
    @Override
    protected void decode(ChannelHandlerContext ctx, GamePacket in, List<Object> out) throws Exception {
        AbstractGamePacketDecoder<?> decoder = protocol.getInboundPacketDecoder(in.getType());
        if (decoder != null) {
            try {
                out.add(decoder.decode(in));
            } catch (Exception e) {
                System.err.println("Error decoding packet: " + in.getType());
                e.printStackTrace();
            }
        } else {
            System.err.println("Unsupported inbound packet type: " + in.getType());
        }
    }
}
