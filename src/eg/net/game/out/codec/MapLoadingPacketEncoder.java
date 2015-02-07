package eg.net.game.out.codec;

import eg.model.Coordinate;
import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.MapLoadingPacket;
import eg.util.io.Buffer;

public final class MapLoadingPacketEncoder implements AbstractGamePacketEncoder<MapLoadingPacket> {
	
	@Override
	public GamePacket encode(MapLoadingPacket packet) throws Exception {
		Coordinate coord = packet.getCoord();
		return new GamePacket(73, new Buffer(4)
				.put128PlusShort(coord.getRegionX())
				.putShort(coord.getRegionY())
				.getData());
	}
}
