package eg.net.game;

import eg.util.net.IsaacCipher;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class GameProtocolEncoder extends MessageToByteEncoder<GamePacket> {
    
    private final GameProtocol protocol;
    private final IsaacCipher cipher;
    
    public GameProtocolEncoder(GameProtocol protocol, IsaacCipher cipher) {
        this.protocol = protocol;
        this.cipher = cipher;
    }
    
    @Override
    protected void encode(ChannelHandlerContext ctx, GamePacket in, ByteBuf out) throws Exception {
        int size = protocol.getOutboundPacketSize(in.getType());
        if (size >= 0 && size != in.getSize()) {
            System.err.println("Missmatch in outbound packet size: " + in.getType());
            return;
        }
        out.writeByte(in.getType() + cipher.nextInt());
        if (size == -1) {
            out.writeByte(in.getSize());
        } else if (size == -2) {
            out.writeShort(in.getSize());
        }
        if (in.getSize() != 0) {
            out.writeBytes(in.getData(), 0, in.getSize());
        }
    }
}
