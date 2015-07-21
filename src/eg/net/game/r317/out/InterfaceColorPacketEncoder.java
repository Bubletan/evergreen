package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.InterfaceColorPacket;
import eg.util.Color;
import eg.util.io.Buffer;

public final class InterfaceColorPacketEncoder implements AbstractGamePacketEncoder<InterfaceColorPacket> {
    
    @Override
    public GamePacket encode(InterfaceColorPacket packet) throws Exception {
        Color color = packet.getColor();
        int red = color.getRed() >> 3;
        int green = color.getGreen() >> 3;
        int blue = color.getBlue() >> 3;
        return new GamePacket(122, new Buffer(4)
                .putAddedLeShort(packet.getId())
                .putAddedLeShort(red << 10 | green << 5 | blue)
                .toData());
    }
}
