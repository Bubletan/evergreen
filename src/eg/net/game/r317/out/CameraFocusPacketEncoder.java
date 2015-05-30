package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.CameraFocusPacket;
import eg.util.io.Buffer;

public final class CameraFocusPacketEncoder implements AbstractGamePacketEncoder<CameraFocusPacket> {
    
    @Override
    public GamePacket encode(CameraFocusPacket packet) throws Exception {
        return new GamePacket(177, new Buffer(6)
                .putByte(packet.getX())
                .putByte(packet.getY())
                .putShort(packet.getHeight())
                .putByte(packet.getTransition())
                .putByte(packet.getGlide())
                .toData());
    }
}
