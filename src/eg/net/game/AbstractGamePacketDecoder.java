package eg.net.game;

@FunctionalInterface
public interface AbstractGamePacketDecoder<T extends AbstractGamePacket> {
	
	public T decode(GamePacket packet) throws Exception;
}
