package eg.net.game.in;

import java.util.Objects;

import eg.net.game.AbstractGamePacket;

public final class CommandPacket implements AbstractGamePacket {
    
    private final String command;
    
    public CommandPacket(String command) {
        this.command = Objects.requireNonNull(command);
    }
    
    public String getCommand() {
        return command;
    }
}
