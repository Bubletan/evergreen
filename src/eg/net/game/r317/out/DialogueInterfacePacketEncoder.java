package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.DialogueInterfacePacket;
import eg.util.io.Buffer;

public final class DialogueInterfacePacketEncoder implements AbstractGamePacketEncoder<DialogueInterfacePacket> {
    
    @Override
    public GamePacket encode(DialogueInterfacePacket packet) throws Exception {
        return new GamePacket(164, new Buffer(2).putLeShort(packet.getId()).toData());
    }
}
