package eg.model.player.div;

import eg.model.item.Item;
import eg.model.item.ItemList;
import eg.util.io.Buffer;

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
	
	private ItemList list = new ItemList(11, ItemList.STACK_ALWAYS);
	
	public Item wornHead() {
		return list.get(HEAD);
	}
	
	public void wearHead(Item item) {
		if (item != null && item.getQuantity() != 1) {
			throw new IllegalArgumentException("Worn items must have a quantity of one.");
		}
		list.set(HEAD, item);
	}
	
	public Item wornCape() {
		return list.get(CAPE);
	}
	
	public void wearCape(Item item) {
		if (item != null && item.getQuantity() != 1) {
			throw new IllegalArgumentException("Worn items must have a quantity of one.");
		}
		list.set(CAPE, item);
	}
	
	public Item wornNeck() {
		return list.get(NECK);
	}
	
	public void wearNeck(Item item) {
		if (item != null && item.getQuantity() != 1) {
			throw new IllegalArgumentException("Worn items must have a quantity of one.");
		}
		list.set(NECK, item);
	}
	
	public Item heldRightHand() {
		return list.get(RIGHT_HAND);
	}

	public void holdRightHand(Item item) {
		list.set(RIGHT_HAND, item);
	}
	
	public Item wornBody() {
		return list.get(BODY);
	}

	public void wearBody(Item item) {
		if (item != null && item.getQuantity() != 1) {
			throw new IllegalArgumentException("Worn items must have a quantity of one.");
		}
		list.set(BODY, item);
	}

	public Item heldLeftHand() {
		return list.get(LEFT_HAND);
	}

	public void holdLeftHand(Item item) {
		list.set(LEFT_HAND, item);
	}

	public Item wornLegs() {
		return list.get(LEGS);
	}

	public void wearLegs(Item item) {
		if (item != null && item.getQuantity() != 1) {
			throw new IllegalArgumentException("Worn items must have a quantity of one.");
		}
		list.set(LEGS, item);
	}

	public Item wornHands() {
		return list.get(HANDS);
	}

	public void wearHands(Item item) {
		if (item != null && item.getQuantity() != 1) {
			throw new IllegalArgumentException("Worn items must have a quantity of one.");
		}
		list.set(HANDS, item);
	}

	public Item wornFeet() {
		return list.get(FEET);
	}

	public void wearFeet(Item item) {
		if (item != null && item.getQuantity() != 1) {
			throw new IllegalArgumentException("Worn items must have a quantity of one.");
		}
		list.set(FEET, item);
	}

	public Item wornRing() {
		return list.get(RING);
	}

	public void wearRing(Item item) {
		if (item != null && item.getQuantity() != 1) {
			throw new IllegalArgumentException("Worn items must have a quantity of one.");
		}
		list.set(RING, item);
	}

	public Item heldAmmo() {
		return list.get(AMMO);
	}

	public void holdAmmo(Item item) {
		list.set(AMMO, item);
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
