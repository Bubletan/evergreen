package eg.net.game.out.codec;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.CameraResetPacket;

public final class CameraResetPacketEncoder implements AbstractGamePacketEncoder<CameraResetPacket> {
    
    @Override
    public GamePacket encode(CameraResetPacket packet) throws Exception {
        return new GamePacket(107);
    }
}
