package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.MapLoadedPacket;

public final class MapLoadedPacketDecoder implements AbstractGamePacketDecoder<MapLoadedPacket> {
    
    @Override
    public MapLoadedPacket decode(GamePacket packet) throws Exception {
        return new MapLoadedPacket();
    }
}
