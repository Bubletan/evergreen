package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.ConfigPacket;
import eg.util.io.Buffer;

public final class ConfigPacketEncoder implements AbstractGamePacketEncoder<ConfigPacket> {
    
    @Override
    public GamePacket encode(ConfigPacket packet) throws Exception {
        Buffer buf = new Buffer().putLeShort(packet.getId());
        int value = packet.getValue();
        if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
            return new GamePacket(36, buf.putByte(value).toData());
        } else {
            return new GamePacket(87, buf.putMeInt(value).toData());
        }
    }
}
