package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.CameraWavePacket;
import eg.util.io.Buffer;

public final class CameraWavePacketEncoder implements AbstractGamePacketEncoder<CameraWavePacket> {
    
    @Override
    public GamePacket encode(CameraWavePacket packet) throws Exception {
        return new GamePacket(35, new Buffer(4)
                .putByte(packet.getType().toInt())
                .putByte(packet.getNoise())
                .putByte(packet.getAmplitude())
                .putByte(packet.getFrequency())
                .toData());
    }
}
