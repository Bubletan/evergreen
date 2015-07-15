package eg.net.game;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
@FunctionalInterface
public interface AbstractGamePacketEncoder<T extends AbstractGamePacket> {
    
    public GamePacket encode(T packet) throws Exception;
}
