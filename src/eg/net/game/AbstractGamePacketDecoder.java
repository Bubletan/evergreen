package eg.net.game;

/**
 * An interface for abstract game packet decoders.
 * 
 * @author Bubletan <https://github.com/Bubletan>
 */
@FunctionalInterface
public interface AbstractGamePacketDecoder<T extends AbstractGamePacket> {
    
    public T decode(GamePacket packet) throws Exception;
}
