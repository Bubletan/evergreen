package eg.net.game.r317.out;

import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.GameMessagePacket;
import eg.util.io.Buffer;

public final class GameMessagePacketEncoder implements AbstractGamePacketEncoder<GameMessagePacket> {
    
    @Override
    public GamePacket encode(GameMessagePacket packet) throws Exception {
        String sender = packet.getSender();
        if (sender.contains(":")) {
            throw new IllegalArgumentException("Sender must not contain a colon.");
        }
        String message = packet.getMessage();
        if (message.endsWith(":tradereq:") || message.endsWith(":duelreq:") || message.endsWith(":chalreq:")) {
            throw new IllegalArgumentException("Message must not end with a reserved tag.");
        }
        String s;
        switch (packet.getType()) {
        case TRADE_REQUEST:
            s = sender + ":tradereq:";
            break;
        case DUEL_REQUEST:
            s = sender + ":duelreq:";
            break;
        case CHALLENGE_REQUEST:
            s = sender + ":" + message + ":chalreq:";
            break;
        default:
            s = message;
        }
        return new GamePacket(253, new Buffer(s.length() + 1).putLine(s).toData());
    }
}
