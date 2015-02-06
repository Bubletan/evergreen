package eg.model.item;

import java.io.BufferedReader;
import java.io.FileReader;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import eg.Config;

public final class ItemType {
	
	private static final ItemType[] cache = new ItemType[Config.N_ITEM_TYPES];

	private int id;
	private String name = "";
	private String desc = "";
	private byte[] descAsBytes;
	private boolean stackable;
	private boolean tradable;
	private boolean notable;
	private int equipSlot;
	private short[] equipBonus;
	
	private ItemType() {
	}
	
	public static ItemType get(int id) {
		Preconditions.checkArgument(id >= 0 && id < cache.length, "ID out of range: " + id);
		ItemType type = cache[id];
		if (type == null) {
			try {
				BufferedReader br = new BufferedReader(new FileReader("./data/config/item/" + id + ".json"));
				type = new Gson().fromJson(br, ItemType.class);
				cache[id] = type;
				type.id = id;
				type.descAsBytes = type.desc.getBytes();
				type.desc = null;
			} catch (Exception e) {
				throw new RuntimeException("Error loading item type: " + id, e);
			}
		}
		return type;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return new String(descAsBytes);
	}
	
	public boolean isStackable() {
		return stackable;
	}
	
	public boolean isTradable() {
		return tradable;
	}
	
	public boolean isNotable() {
		return notable;
	}
	
	public boolean isEquipable() {
		return equipSlot != -1;
	}
	
	public int getEquipSlot() {
		return equipSlot;
	}
	
	public short[] getEquipBonuses() {
		return equipBonus;
	}
}
