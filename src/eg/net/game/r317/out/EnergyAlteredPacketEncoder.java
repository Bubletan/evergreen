package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.EnergyAlteredPacket;
import eg.util.io.Buffer;

public final class EnergyAlteredPacketEncoder implements AbstractGamePacketEncoder<EnergyAlteredPacket> {
    
    @Override
    public GamePacket encode(EnergyAlteredPacket packet) throws Exception {
        return new GamePacket(110, new Buffer(1).putByte(packet.getEnergy()).toData());
    }
}
