package eg.game.event.player.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eg.game.event.player.PlayerEvent;
import eg.game.model.player.Player;

public final class CommandEvent extends PlayerEvent {
    
    private static final Pattern PATTERN = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
    
    private final String command;
    private List<String> commandParts;

    public CommandEvent(Player author, String command) {
        super(author);
        this.command = command;
    }
    
    public String getCommand() {
        return command;
    }
    
    private void computeNameAndArguments() {
        commandParts = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(command);
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                commandParts.add(matcher.group(1));
            } else if (matcher.group(2) != null) {
                commandParts.add(matcher.group(2));
            } else {
                commandParts.add(matcher.group());
            }
        } 
    }
    
    public String getCommandName() {
        if (commandParts == null) {
            computeNameAndArguments();
        }
        return !commandParts.isEmpty() ? commandParts.get(0) : "";
    }
    
    public int getCommandArgumentCount() {
        if (commandParts == null) {
            computeNameAndArguments();
        }
        return Math.max(commandParts.size() - 1, 0);
    }
    
    public String getCommandArgument(int index) {
        if (commandParts == null) {
            computeNameAndArguments();
        }
        return ++index < commandParts.size() ? commandParts.get(index) : null;
    }
}
