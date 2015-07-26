package eg.game.model.player;

import java.util.Objects;

import eg.game.model.MobileEntity;
import eg.game.model.IdleAnimation;
import eg.game.model.PrayerHeadicon;
import eg.game.model.SkullHeadicon;
import eg.game.model.item.inv.AlwaysStackingInventory;
import eg.game.model.item.inv.Inventory;
import eg.game.model.item.inv.SelectivelyStackingInventory;
import eg.game.world.sync.SyncBlock;
import eg.game.world.sync.SyncContext;
import eg.net.game.GameSession;
import eg.util.UsernameCodec;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public final class Player extends MobileEntity {
    
    private String username;
    private String password;
    private int privilege = 2; // TODO: Remove default 2
    
    private final Identikit identikit = new Identikit();
    private final Equipment equipment = new Equipment();
    private final Inventory inv = new SelectivelyStackingInventory(28);
    private final Inventory bank = new AlwaysStackingInventory(352);
    private final SkillSet stats = new SkillSet();
    private final SyncContext syncContext = new SyncContext();
    
    private final GameSession session;
    
    private IdleAnimation idleAnimation = IdleAnimation.DEFAULT;
    private PrayerHeadicon prayerHeadicon = PrayerHeadicon.NONE;
    private SkullHeadicon skullHeadicon = SkullHeadicon.NONE;
    
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
        return UsernameCodec.encode(username);
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
    
    public Inventory getBank() {
        return bank;
    }
    
    public SkillSet getStatistics() {
        return stats;
    }
    
    public SyncContext getSyncContext() {
        return syncContext;
    }
    
    public IdleAnimation getIdleAnimation() {
        return idleAnimation;
    }
    
    public void setIdleAnimation(IdleAnimation idleAnimation) {
        this.idleAnimation = Objects.requireNonNull(idleAnimation);
    }
    
    public PrayerHeadicon getPrayerHeadicon() {
        return prayerHeadicon;
    }
    
    public void setPrayerHeadicon(PrayerHeadicon prayerHeadicon) {
        this.prayerHeadicon = Objects.requireNonNull(prayerHeadicon);
    }
    
    public SkullHeadicon getSkullHeadicon() {
        return skullHeadicon;
    }
    
    public void setSkullHeadicon(SkullHeadicon skullHeadicon) {
        this.skullHeadicon = Objects.requireNonNull(skullHeadicon);
    }
    
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
