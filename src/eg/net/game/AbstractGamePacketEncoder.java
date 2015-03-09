package eg.net.game;

@FunctionalInterface
public interface AbstractGamePacketEncoder<T extends AbstractGamePacket> {
    
    public GamePacket encode(T packet) throws Exception;
}
