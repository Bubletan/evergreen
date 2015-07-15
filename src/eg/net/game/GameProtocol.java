package eg.net.game;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public interface GameProtocol {
    
    public int getInboundPacketSize(int type);
    
    public int getOutboundPacketSize(int type);
    
    public <T extends AbstractGamePacket> AbstractGamePacketDecoder<T> getInboundPacketDecoder(int type);
    
    public <T extends AbstractGamePacket> AbstractGamePacketEncoder<T> getOutboundPacketEncoder(Class<T> type);
}
