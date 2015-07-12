package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.GameMessagePacket;
import eg.util.io.Buffer;

public final class GameMessagePacketEncoder implements AbstractGamePacketEncoder<GameMessagePacket> {
    
    @Override
    public GamePacket encode(GameMessagePacket packet) throws Exception {
        String message = packet.getMessage();
        if (message.endsWith(":tradereq:") || message.endsWith(":duelreq:") || message.endsWith(":chalreq:")) {
            throw new IllegalArgumentException("Message must not end with a reserved tag.");
        }
        switch (packet.getType()) {
        case TRADE_REQUEST:
            message = message.concat(":tradereq:");
            break;
        case DUEL_REQUEST:
            message = message.concat(":duelreq:");
            break;
        case CHALLENGE_REQUEST:
            message = message.concat(":chalreq:");
            break;
        default:
        }
        return new GamePacket(253, new Buffer(message.length() + 1).putLine(message).toData());
    }
}
