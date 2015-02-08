package eg.model.player.div;

import eg.model.item.Item;
import eg.model.item.ItemContainer;

public final class Equipment {
	
	private static final int HEAD = 0;
	private static final int CAPE = 1;
	private static final int NECK = 2;
	private static final int RIGHT_HAND = 3;
	private static final int BODY = 4;
	private static final int LEFT_HAND = 5;
	private static final int LEGS = 6;
	private static final int HANDS = 7;
	private static final int FEET = 8;
	private static final int RING = 9;
	private static final int AMMO = 10;
	
	private ItemContainer list = new ItemContainer(11, ItemContainer.STACK_ALWAYS);
	
	private int stabAttackBonus;
	private int slashAttackBonus;
	private int crushAttackBonus;
	private int magicAttackBonus;
	private int rangedAttackBonus;
	private int stabDefenceBonus;
	private int slashDefenceBonus;
	private int crushDefenceBonus;
	private int magicDefenceBonus;
	private int rangedDefenceBonus;
	private int strengthBonus;
	private int prayerBonus;
	
	public Item getHeadwear() {
		return null;
	}
	
	public Item getBackwear() {
		return null;
	}
	
	public Item getNeckwear() {
		return null;
	}
	
	public Item getRightHand() {
		return null;
	}
	
	public Item getTop() {
		return null;
	}
	
	public Item getLeftHand() {
		return null;
	}
	
	public Item getBottom() {
		return null;
	}
	
	public Item getHandwear() {
		return null;
	}
	
	public Item getFootwear() {
		return null;
	}
	
	public Item getRing() {
		return null;
	}
	
	public Item getAmmo() {
		return null;
	}
	
	public int getStabAttackBonus() {
		return stabAttackBonus;
	}
	
	public int getSlashAttackBonus() {
		return slashAttackBonus;
	}
	
	public int getCrushAttackBonus() {
		return crushAttackBonus;
	}
	
	public int getMagicAttackBonus() {
		return magicAttackBonus;
	}
	
	public int getRangedAttackBonus() {
		return rangedAttackBonus;
	}
	
	public int getStabDefenceBonus() {
		return stabDefenceBonus;
	}
	
	public int getSlashDefenceBonus() {
		return slashDefenceBonus;
	}
	
	public int getCrushDefenceBonus() {
		return crushDefenceBonus;
	}
	
	public int getMagicDefenceBonus() {
		return magicDefenceBonus;
	}
	
	public int getRangedDefenceBonus() {
		return rangedDefenceBonus;
	}
	
	public int getStrengthBonus() {
		return strengthBonus;
	}
	
	public int getPrayerBonus() {
		return prayerBonus;
	}

	public boolean hasFullHelm() {
		return false;
	} 
	
	public boolean hasFullMask() {
		return false;
	}
	
	public boolean hasFullBody() {
		return false;
	}
}
 