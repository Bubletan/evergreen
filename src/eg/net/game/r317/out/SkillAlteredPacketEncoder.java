package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.SkillAlteredPacket;
import eg.util.io.Buffer;

public final class SkillAlteredPacketEncoder implements AbstractGamePacketEncoder<SkillAlteredPacket> {
    
    @Override
    public GamePacket encode(SkillAlteredPacket packet) throws Exception {
        return new GamePacket(134, new Buffer(6).putByte(packet.getId())
                .putRmeInt(packet.getExperience()).putByte(packet.getLevel()).toData());
    }
}
