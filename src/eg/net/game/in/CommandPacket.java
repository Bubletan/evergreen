package eg.net.game.in;

import eg.net.game.AbstractGamePacket;

public final class CommandPacket implements AbstractGamePacket {
    
    private final String command;
    
    public CommandPacket(String command) {
        this.command = command;
    }
    
    public String getCommand() {
        return command;
    }
}
