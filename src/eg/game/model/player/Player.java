package eg.game.model.player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import eg.Config;
import eg.game.model.Charactor;
import eg.game.model.Coordinate;
import eg.game.model.IdleAnimation;
import eg.game.model.Path;
import eg.game.model.PathBuilder;
import eg.game.model.npc.Npc;
import eg.game.sync.SyncBlock;
import eg.net.game.GameSession;
import eg.net.game.in.ButtonPacket;
import eg.net.game.in.ChatMessagePacket;
import eg.net.game.in.CommandPacket;
import eg.net.game.in.MovementPacket;
import eg.net.game.out.CameraResetPacket;
import eg.net.game.out.GameMessagePacket;
import eg.net.game.out.InterfaceTextPacket;
import eg.net.game.out.MainInterfacePacket;
import eg.net.game.out.MulticombatOverlayPacket;
import eg.net.game.out.PlayerInitPacket;
import eg.net.game.out.SidebarInterfacePacket;
import eg.net.game.out.SystemUpdatePacket;
import eg.util.ChatMessageUtils;
import eg.util.Misc;

public final class Player extends Charactor {
    
    private String username;
    private String password;
    private int privilege = 2; // TODO: Remove default 2
    
    private final Identikit identikit = new Identikit();
    private final Equipment equipment = new Equipment();
    private final Inventory inv = new Inventory();
    private final Bank bank = new Bank();
    private final Statistics stats = new Statistics();
    private final IdleAnimation idleAnimation = new IdleAnimation();
    
    private final GameSession session;
    
    private final List<Player> localPlayerList = new LinkedList<>();
    private final List<Npc> localNpcList = new LinkedList<>();
    private final Map<Integer, Integer> localAppearanceCycleMap = new HashMap<>();
    private int appearanceCycle;
    
    public List<Player> getLocalPlayerList() {
        return localPlayerList;
    }
    
    public List<Npc> getLocalNpcList() {
        return localNpcList;
    }
    
    public Map<Integer, Integer> getLocalAppearanceCycleMap() {
        return localAppearanceCycleMap;
    }
    
    public int getAppearanceCycle() {
        return appearanceCycle;
    }
    
    public void setAppearanceCycle(int value) {
        appearanceCycle = value;
    }
    
    public Player(GameSession session, String username, String password) {
        this.session = session;
        this.username = username;
        this.password = password;
        getSyncBlockSet().add(new SyncBlock.Appearance(this));
    }
    
    public GameSession getSession() {
        return session;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getDatabaseUsername() {
        return username.toLowerCase().replace(" ", "_");
    }
    
    public long getHash() {
        return Misc.encryptUsername(username);
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isMember() {
        return true;
    }
    
    public int getPrivilege() {
        return privilege;
    }
    
    public Identikit getIdentikit() {
        return identikit;
    }
    
    public Equipment getEquipment() {
        return equipment;
    }
    
    public Inventory getInventory() {
        return inv;
    }
    
    public Bank getBank() {
        return bank;
    }
    
    public Statistics getStatistics() {
        return stats;
    }
    
    public IdleAnimation getIdleAnimation() {
        return idleAnimation;
    }
    
    public int getViewingDistance() {
        return 15;
    }
    
    public void initialize() {
        
        session.send(new PlayerInitPacket(this));
        session.send(new CameraResetPacket());
        
        message(Config.WELCOMING_MESSAGE);
        
        sidebar(0, 2423);
        sidebar(1, 3917);
        sidebar(2, 638);
        sidebar(3, 3213);
        sidebar(4, 1644);
        sidebar(5, 5608);
        sidebar(6, 1151);
        // sidebar(7, 2423);
        sidebar(8, 5065);
        sidebar(9, 5715);
        sidebar(10, 2449);
        sidebar(11, 4445);
        sidebar(12, 147);
        sidebar(13, 6299);
    }
    
    public void process() {
        session.receive().stream().forEach(packet -> {
            if (packet instanceof ChatMessagePacket) {
                ChatMessagePacket p = (ChatMessagePacket) packet;
                String decodedMsg = ChatMessageUtils.decode(p.getEncodedMessage());
                byte[] encodedMsg = ChatMessageUtils.encode(decodedMsg);
                getSyncBlockSet().add(new SyncBlock.ChatMessage(encodedMsg, p.getColorEffect(),
                        p.getAnimationEffect(), getPrivilege()));
            } else if (packet instanceof CommandPacket) {
                String[] cmd = ((CommandPacket) packet).getCommand().split(" ");
                switch (cmd[0]) {
                case "tele":
                    try {
                        int x = Integer.parseInt(cmd[1]);
                        int y = Integer.parseInt(cmd[2]);
                        int height = cmd.length >= 4 ? Integer.parseInt(cmd[3]) : 0;
                        getMovement().setCoordinate(new Coordinate(x, y, height));
                    } catch (Exception e) {
                        message("Incorrect syntax.");
                    }
                    break;
                }
            } else if (packet instanceof ButtonPacket) {
            } else if (packet instanceof MovementPacket) {
                MovementPacket p = (MovementPacket) packet;
                getMovement().setRunningEnabled(p.isCtrlRun());
                PathBuilder pb = new PathBuilder();
                for (int i = 0, n = p.getJumpPointCount(); i < n; i++) {
                    pb.appendJumpPoint(new Path.Point(p.getJumpPointX(i), p.getJumpPointY(i)));
                }
                getMovement().setPath(pb.toPath());
            }
        });
    }
    
    public void sidebar(int index, int interfaceid) {
        session.send(new SidebarInterfacePacket(index, interfaceid));
    }
    
    public void message(String msg) {
        session.send(new GameMessagePacket(msg));
    }
    
    public void update(int seconds) {
        session.send(new SystemUpdatePacket(seconds));
    }
    
    public void maininterface(int id) {
        session.send(new MainInterfacePacket(id));
    }
    
    public void interfacetext(String text, int id) {
        session.send(new InterfaceTextPacket(id, text));
    }
    
    public void multiway(boolean enabled) {
        session.send(new MulticombatOverlayPacket(enabled));
    }
    
    public int headIcon;
    public int headIconPk;
    
    @Override
    public int hashCode() {
        return username.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [username=" + username + "]";
    }
}
