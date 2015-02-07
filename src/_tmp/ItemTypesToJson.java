package _tmp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

public class ItemTypesToJson {
	
	public static void main(String[] args) {
		
		XStream xStream = new XStream();
		xStream.alias("itemDefinition", ItemDefinition.class);
		List<ItemDefinition> list = (List<ItemDefinition>) xStream.fromXML(ItemDefinition.class.getResourceAsStream("itemDefinitions.xml"));
	
		System.out.println("Loaded XML!");
		
		Map<Integer, ItemType> map = new HashMap<>();
		for (ItemDefinition def : list) {
			ItemType type = new ItemType();
			
			type.name = def.getName();
			type.desc = def.getDescription();
			type.members = def.isMembersOnly();
			type.tradable = !def.isUntradable();
			if (def.isNoted() || def.isNoteable()) {
				type.asNonStackable = def.getNormalId();
				type.asStackable = def.getNotedId();
				if (def.isNoted()) {
					type.asStackable = def.getId();
				} else {
					type.asNonStackable = def.getId();
				}
			} else {
				if (def.isStackable()) {
					type.asNonStackable = -1;
					type.asStackable = def.getId();
				} else {
					type.asStackable = -1;
					type.asNonStackable = def.getId();
				}
			}
			type.value = def.getPrice();
			type.highAlch = def.getHighAlcValue();
			type.lowAlch = def.getLowAlcValue();
			switch (def.getEquipmentType().toUpperCase()) {
			case "NONE": type.equipSlot = -1; break;
			case "HAT": type.equipSlot = 0; break;
			case "CAPE": type.equipSlot = 1; break;
			case "AMULET": type.equipSlot = 2; break;
			case "WEAPON": type.equipSlot = 3; break;
			case "BODY": type.equipSlot = 4; break;
			case "SHIELD": type.equipSlot = 5; break;
			case "LEGS": type.equipSlot = 6; break;
			case "GLOVES": type.equipSlot = 7; break;
			case "BOOTS": type.equipSlot = 8; break;
			case "RING": type.equipSlot = 9; break;
			case "ARROWS": type.equipSlot = 10; break;
			default: System.out.println("Unknown eq type: " + def.getEquipmentType().toUpperCase());
			}
			type.equipBonus = def.getBonuses();
			
			map.put(def.getId(), type);
		}
		
		System.out.println("Converted objects!");
		
		try (Scanner scanner = new Scanner(new FileReader("weight.txt"))) {
			while (scanner.hasNextLine()) {
				String[] line = scanner.nextLine().split(" ");
				int id = Integer.parseInt(line[0]);
				double w = Double.parseDouble(line[1]);
				map.get(id).weight = w;
			}
		} catch (FileNotFoundException e) {
		}
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String ls = System.getProperty("line.separator");
		for (Map.Entry<Integer, ItemType> entry : map.entrySet()) {
			String json = gson.toJson(entry.getValue()).replace("\n", ls);
			try (PrintStream out = new PrintStream(new FileOutputStream("./data/config/item/" + entry.getKey() + ".json"))) {
			    out.print(json);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Done!");
		System.out.println(list.size());
	}
}
