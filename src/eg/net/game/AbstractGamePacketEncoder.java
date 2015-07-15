package eg.net.game;

/**
 * An interface for abstract game packet encoders.
 * 
 * @author Bubletan <https://github.com/Bubletan>
 */
@FunctionalInterface
public interface AbstractGamePacketEncoder<T extends AbstractGamePacket> {
    
    public GamePacket encode(T packet) throws Exception;
}
