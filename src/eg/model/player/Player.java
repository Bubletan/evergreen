package eg.model.player;

import eg.Config;
import eg.global.World;
import eg.model.Charactor;
import eg.model.Coordinate;
import eg.model.IdleAnimation;
import eg.model.MovementProvider;
import eg.model.player.div.Bank;
import eg.model.player.div.Equipment;
import eg.model.player.div.Identikit;
import eg.model.player.div.Inventory;
import eg.model.player.div.Statistics;
import eg.model.req.Animation;
import eg.model.req.ForceChatMessage;
import eg.model.sync.SyncBlock;
import eg.model.sync.SyncBlockSet;
import eg.net.game.GameSession;
import eg.net.game.in.ButtonPacket;
import eg.net.game.in.ChatMessagePacket;
import eg.net.game.in.CommandPacket;
import eg.net.game.in.MovementPacket;
import eg.net.game.out.CameraResetPacket;
import eg.net.game.out.InterfaceTextPacket;
import eg.net.game.out.MainInterfacePacket;
import eg.net.game.out.GameMessagePacket;
import eg.net.game.out.MulticombatOverlayPacket;
import eg.net.game.out.PlayerInitPacket;
import eg.net.game.out.SidebarInterfacePacket;
import eg.net.game.out.SystemUpdatePacket;
import eg.util.Misc;

public final class Player extends Charactor {
	
	private String username;
	private String password;
	private int privilege = 2; //TODO: Remove default 2
	
	private final Identikit identikit = new Identikit();
	private final Equipment equipment = new Equipment();
	private final Inventory inv = new Inventory();
	private final Bank bank = new Bank();
	private final Statistics stats = new Statistics();
	private final IdleAnimation idleAnimation = new IdleAnimation();
	
	private SyncBlockSet syncBlockSet = new SyncBlockSet();
	{
		syncBlockSet.add(new SyncBlock.Appearance(this));
	}
	
	private final GameSession session;
	
	private boolean active;
	
	private final PlayerList localPlayers = new PlayerList(0xff);
	
	public PlayerList getLocalPlayers() {
		return localPlayers;
	}
	
	private final Movement movement;
	
	public Player(GameSession session, String username, String password) {
		this.session = session;
		this.username = username;
		this.password = password;
		setCoordinate(new Coordinate(3200, 3200));
		movement = new Movement(this);
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
		if (active) {
			World.getWorld().addPlayer(this);
		} else {
			World.getWorld().removePlayer(this);
		}
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
	
	public Movement getMovement() {
		return movement;
	}
	
	public int getViewingDistance() {
		return 15;
	}
	
	public SyncBlockSet getSyncBlockSet() {
		return syncBlockSet;
	}
	
	public void resetSyncBlockSet() {
		syncBlockSet = new SyncBlockSet();
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
		//sidebar(7, 2423);
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
				syncBlockSet.add(new SyncBlock.ChatMessage(p.getCompressedMessage(), p.getColorEffect(),
						p.getAnimationEffect(), getPrivilege()));
			} else if (packet instanceof CommandPacket) {
				syncBlockSet.add(new SyncBlock.ForceChatMessage(new ForceChatMessage("Hi!", true)));
				syncBlockSet.add(new SyncBlock.Animation(new Animation(231)));
			} else if (packet instanceof ButtonPacket) {
				
			} else if (packet instanceof MovementPacket) {
				MovementPacket p = (MovementPacket) packet;
				MovementProvider mp = movement.getProvider();
				mp.beginPath(p.isCtrlRun());
				for (int i = 0, n = p.getJumpPointCount(); i < n; i++) {
					mp.addJumpPoint(p.getJumpPointX(i), p.getJumpPointY(i));
				}
				mp.endPath();
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
}
