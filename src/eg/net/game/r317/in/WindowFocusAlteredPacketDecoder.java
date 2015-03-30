package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.WindowFocusAlteredPacket;

public final class WindowFocusAlteredPacketDecoder implements AbstractGamePacketDecoder<WindowFocusAlteredPacket> {
    
    @Override
    public WindowFocusAlteredPacket decode(GamePacket packet) throws Exception {
        boolean focused = packet.toBuffer().getBoolean();
        return new WindowFocusAlteredPacket(focused);
    }
}
