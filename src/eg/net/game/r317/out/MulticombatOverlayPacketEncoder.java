package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.MulticombatOverlayPacket;
import eg.util.io.Buffer;

public final class MulticombatOverlayPacketEncoder implements AbstractGamePacketEncoder<MulticombatOverlayPacket> {
    
    @Override
    public GamePacket encode(MulticombatOverlayPacket packet) throws Exception {
        return new GamePacket(61, new Buffer(1).putBoolean(packet.isEnabled()).getData());
    }
}
