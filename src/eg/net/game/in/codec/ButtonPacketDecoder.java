package eg.net.game.in.codec;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.ButtonPacket;

public final class ButtonPacketDecoder implements AbstractGamePacketDecoder<ButtonPacket> {
    
    @Override
    public ButtonPacket decode(GamePacket packet) throws Exception {
        return new ButtonPacket(packet.toBuffer().getUShort());
    }
}
