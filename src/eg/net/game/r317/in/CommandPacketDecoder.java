package eg.net.game.r317.in;

import eg.net.game.AbstractGamePacketDecoder;
import eg.net.game.GamePacket;
import eg.net.game.in.CommandPacket;

public final class CommandPacketDecoder implements AbstractGamePacketDecoder<CommandPacket> {
    
    @Override
    public CommandPacket decode(GamePacket packet) throws Exception {
        return new CommandPacket(packet.toBuffer().getLine());
    }
}
