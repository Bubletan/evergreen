package eg.net.game.in.codec;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.CommandPacket;

public final class CommandPacketDecoder implements AbstractGamePacketDecoder<CommandPacket> {
    
    @Override
    public CommandPacket decode(GamePacket packet) throws Exception {
        return new CommandPacket(packet.toBuffer().getLine());
    }
}
