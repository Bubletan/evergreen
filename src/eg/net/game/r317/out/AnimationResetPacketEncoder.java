package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.AnimationResetPacket;

public final class AnimationResetPacketEncoder implements AbstractGamePacketEncoder<AnimationResetPacket> {
    
    @Override
    public GamePacket encode(AnimationResetPacket packet) throws Exception {
        return new GamePacket(1);
    }
}
