package eg.model.item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;

import eg.Config;

public final class ItemType {
	
	private static final ItemType[] cache = new ItemType[Config.N_ITEM_TYPES];
	
	private static final BiMap<ItemType, ItemType> toNote = HashBiMap.create();
	private static final BiMap<ItemType, ItemType> toNonNote = toNote.inverse();

	private int id;
	private String name;
	private String desc;
	
	private boolean members;
	private boolean stackable;
	private boolean tradable;
	private int note = -1;
	
	private ItemType() {
	}
	
	public static void load() {
		int count = 0;
		
		Gson gson = new Gson();
		for (File file : new File("./data/config/item/").listFiles()) {
			
			String name = file.getName().toLowerCase();
			if (!name.endsWith(".json")) {
				continue;
			}
			
			try {
				int id = Integer.parseInt(name.substring(0, name.length() - 5));
				
				BufferedReader br = new BufferedReader(new FileReader(file));
				ItemType type = gson.fromJson(br, ItemType.class);
				
				type.id = id;
				cache[id] = type;
				
				count++;
				
			} catch (Exception e) {
			}
		}
		
		for (ItemType type : cache) {
			if (type == null || type.note == -1) {
				continue;
			}
			if (cache[type.note] != null) {
				toNote.put(type, cache[type.note]);
			} else {
				ItemType note = new ItemType();
				note.id = type.note;
				note.name = type.name;
				note.members = type.members;
				note.tradable = type.tradable;
				note.stackable = true;
				String prefix = "a";
				char c = Character.toLowerCase(type.name.charAt(0));
				if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
					prefix = "an";
				}
				note.desc = "Swap this note at any bank for " + prefix + " " + type.name + ".";
				toNote.put(type, note);
				
				count++;
			}
		}
		
		System.out.println("Registered " + count + " out of " + cache.length + " item types.");
	}
	
	public static ItemType get(int id) {
		Preconditions.checkElementIndex(id, cache.length, "ID out of bounds: " + id);
		return cache[id];
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
	
	public boolean isMembersOnly() {
		return members;
	}
	
	public boolean isStackable() {
		return stackable;
	}
	
	public boolean isTradable() {
		return tradable;
	}
	
	public boolean isNotable() {
		return note != -1;
	}
	
	public boolean isNote() {
		return toNonNote.containsKey(this);
	}
	
	public ItemType toNote() {
		ItemType type = toNote.get(this);
		Preconditions.checkNotNull(type, "Item cannot be noted.");
		return type;
	}
	
	public ItemType toNonNote() {
		ItemType type = toNonNote.get(this);
		Preconditions.checkNotNull(type, "Item cannot be unnoted.");
		return type;
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
