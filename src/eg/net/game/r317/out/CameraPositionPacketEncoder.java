package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.CameraPositionPacket;
import eg.util.io.Buffer;

public class CameraPositionPacketEncoder implements AbstractGamePacketEncoder<CameraPositionPacket> {
    
    @Override
    public GamePacket encode(CameraPositionPacket packet) throws Exception {
        return new GamePacket(166, new Buffer(6)
                .putByte(packet.getX())
                .putByte(packet.getY())
                .putShort(packet.getHeight())
                .putByte(packet.getTransition())
                .putByte(packet.getGlide())
                .toData());
    }
}
