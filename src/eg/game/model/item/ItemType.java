package eg.game.model.item;

import java.io.BufferedReader;
import java.io.FileReader;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import eg.Config;

public final class ItemType {
	
	private static final ItemType[] cache = new ItemType[Config.N_ITEM_TYPES];

	private transient int id;
	
	private String name;
	private String desc;
	
	private boolean members;
	private boolean tradable;
	
	private int asNonStackable;
	private int asStackable;
	
	private int value;
	private int highAlch;
	private int lowAlch;
	
	private double weight;
	private int equipSlot;
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
	
	private ItemType() {
	}
	
	public static ItemType get(int id) {
		Preconditions.checkElementIndex(id, cache.length, "ID out of bounds: " + id);
		ItemType type = cache[id];
		if (type != null) {
			return type;
		}
		synchronized (cache) {
			type = cache[id];
			if (type != null) {
				return type;
			}
			try {
				BufferedReader br = new BufferedReader(new FileReader("./data/config/item/" + id + ".json"));
				type = new Gson().fromJson(br, ItemType.class);
				type.id = id;
				cache[id] = type;
				return type;
			} catch (Exception e) {
				throw new RuntimeException("Error loading item type: " + id);
			}
		}
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return desc;
	}
	
	public boolean isMembers() {
		return members;
	}
	
	public boolean isTradable() {
		return tradable;
	}
	
	public boolean isStackable() {
		return id == asStackable;
	}
	
	public boolean isNotable() {
		return id == asNonStackable && asStackable != -1;
	}
	
	public boolean isNote() {
		return id == asStackable && asNonStackable != -1;
	}
	
	public ItemType toNote() {
		Preconditions.checkState(isNotable(), "Item cannot be noted.");
		return get(asStackable);
	}
	
	public ItemType toNonNote() {
		Preconditions.checkState(isNote(), "Item cannot be unnoted.");
		return get(asNonStackable);
	}
	
	public double getWeight() {
		return weight;
	}
	
	public boolean isEquipable() {
		return equipSlot != -1;
	}
	
	public int getEquipSlot() {
		return equipSlot;
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
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj == this;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "[" + "id=" + id + "]";
	}
}
