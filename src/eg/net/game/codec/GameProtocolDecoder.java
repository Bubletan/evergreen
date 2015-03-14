package eg.net.game.codec;

import java.util.List;

import eg.net.game.GamePacket;
import eg.net.game.GamePacketConstants;
import eg.util.io.IsaacCipher;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public final class GameProtocolDecoder extends ByteToMessageDecoder {
    
    private final IsaacCipher cipher;
    private int packetType = -1;
    private int packetSize;
    
    public GameProtocolDecoder(IsaacCipher cipher) {
        this.cipher = cipher;
    }
    
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int available = in.readableBytes();
        if (available == 0) {
            return;
        }
        do {
            if (packetType == -1 && available >= 1) {
                packetType = in.readByte() - cipher.nextInt() & 0xff;
                packetSize = GamePacketConstants.INCOMING_PACKET_SIZE[packetType];
                available--;
            }
            if (packetSize == -1) {
                if (available >= 1) {
                    packetSize = in.readUnsignedByte();
                    available--;
                } else {
                    return;
                }
            } else if (packetSize == -2) {
                if (available >= 2) {
                    packetSize = in.readUnsignedShort();
                    available -= 2;
                } else {
                    return;
                }
            }
            if (available < packetSize) {
                return;
            }
            byte[] data = new byte[packetSize];
            in.readBytes(data);
            out.add(new GamePacket(packetType, data));
            available -= packetSize;
            packetType = -1;
        } while (available > 0);
    }
}
