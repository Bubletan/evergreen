package eg.net.game;

public interface GameProtocol {
    
    public int getInboundPacketSize(int type);
    
    public int getOutboundPacketSize(int type);
    
    public <T extends AbstractGamePacket> AbstractGamePacketDecoder<T> getInboundPacketDecoder(int type);
    
    public <T extends AbstractGamePacket> AbstractGamePacketEncoder<T> getOutboundPacketEncoder(Class<T> type);
}
