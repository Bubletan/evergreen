package eg.net.game.r317.out;

import java.util.EnumMap;
import java.util.Map;

import eg.game.model.PrivacyLevel;
import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.PrivacyConfigPacket;
import eg.util.io.Buffer;

public final class PrivacyConfigPacketEncoder implements AbstractGamePacketEncoder<PrivacyConfigPacket> {
    
    private static final Map<PrivacyLevel, Integer> privacyLevelToIntMap = new EnumMap<>(PrivacyLevel.class);
    
    static {
        privacyLevelToIntMap.put(PrivacyLevel.ON, 0);
        privacyLevelToIntMap.put(PrivacyLevel.FRIENDS, 1);
        privacyLevelToIntMap.put(PrivacyLevel.OFF, 2);
        privacyLevelToIntMap.put(PrivacyLevel.HIDE, 3);
    }
    
    @Override
    public GamePacket encode(PrivacyConfigPacket packet) throws Exception {
        return new GamePacket(206, new Buffer(3)
                .putByte(privacyLevelToIntMap.get(packet.getPublicMode()))
                .putByte(privacyLevelToIntMap.get(packet.getPrivateMode()))
                .putByte(privacyLevelToIntMap.get(packet.getTradeMode()))
                .toData());
    }
}
