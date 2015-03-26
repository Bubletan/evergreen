package eg.game.model.player;

import eg.game.world.Coordinate;
import eg.game.world.path.Path;
import eg.game.world.path.PathBuilder;
import eg.game.world.sync.SyncBlock;
import eg.net.game.in.ButtonPacket;
import eg.net.game.in.ChatMessagePacket;
import eg.net.game.in.CommandPacket;
import eg.net.game.in.MovementPacket;
import eg.net.game.out.GameMessagePacket;
import eg.net.game.out.MainInterfacePacket;
import eg.util.ChatMessageUtils;
import eg.util.task.Task;

public final class PlayerProcessTask implements Task {
    
    private final Player player;
    
    public PlayerProcessTask(Player player) {
        this.player = player;
    }
    
    @Override
    public void execute() {
        
        player.getSession().receive().stream().forEach(packet -> {
            if (packet instanceof ChatMessagePacket) {
                
                ChatMessagePacket p = (ChatMessagePacket) packet;
                String decodedMsg = ChatMessageUtils.decode(p.getEncodedMessage());
                byte[] encodedMsg = ChatMessageUtils.encode(decodedMsg);
                player.getSyncBlockSet().add(new SyncBlock.ChatMessage(encodedMsg, p.getColorEffect(),
                        p.getAnimationEffect(), player.getPrivilege()));
                
            } else if (packet instanceof CommandPacket) {
                
                String[] cmd = ((CommandPacket) packet).getCommand().split(" ");
                switch (cmd[0]) {
                
                case "tele":
                    try {
                        int x = Integer.parseInt(cmd[1]);
                        int y = Integer.parseInt(cmd[2]);
                        int height = cmd.length >= 4 ? Integer.parseInt(cmd[3]) : 0;
                        player.getMovement().setCoordinate(new Coordinate(x, y, height));
                    } catch (Exception e) {
                        player.getSession().send(new GameMessagePacket("error - ::tele x y [height]"));
                    }
                    break;
                    
                case "up":
                case "down":
                    try {
                        player.getMovement().setCoordinate(new Coordinate(player.getCoordinate().getX(),
                                player.getCoordinate().getY(),
                                player.getCoordinate().getHeight() + (cmd[0].equals("up") ? 1 : -1)));
                    } catch (Exception e) {
                        player.getSession().send(new GameMessagePacket("error - ::up / ::down - height out of bounds"));
                    }
                    break;
                    
                case "if":
                    try {
                        int id = Integer.parseInt(cmd[1]);
                        player.getSession().send(new MainInterfacePacket(id));
                    } catch (Exception e) {
                        player.getSession().send(new GameMessagePacket("error - ::if id"));
                    }
                    break;
                }
                
            } else if (packet instanceof ButtonPacket) {
                
            } else if (packet instanceof MovementPacket) {
                
                MovementPacket p = (MovementPacket) packet;
                player.getMovement().setRunningEnabled(p.isCtrlRun());
                PathBuilder pb = new PathBuilder();
                for (int i = 0, n = p.getJumpPointCount(); i < n; i++) {
                    pb.appendJumpPoint(new Path.Point(p.getJumpPointX(i), p.getJumpPointY(i)));
                }
                player.getMovement().setPath(pb.toPath());
            }
        });
    }
}
