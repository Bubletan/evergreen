package eg.net.game.out.codec;

import eg.game.world.Coordinate;
import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.MapLoadingPacket;
import eg.util.io.Buffer;

public final class MapLoadingPacketEncoder implements AbstractGamePacketEncoder<MapLoadingPacket> {
    
    @Override
    public GamePacket encode(MapLoadingPacket packet) throws Exception {
        Coordinate coord = packet.getCoordinate();
        return new GamePacket(73, new Buffer(4).putAddedShort(coord.getX() >> 3)
                .putShort(coord.getY() >> 3).getData());
    }
}
