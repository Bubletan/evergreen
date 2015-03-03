package eg.model.player;

public final class Identikit {
	
	public static final Identikit DEFAULT = new Identikit();
	
	private static final int DEFAULT_GENDER = 0;
	private static final int DEFAULT_HEAD = 7;
	private static final int DEFAULT_TORSO = 25;
	private static final int DEFAULT_ARMS = 29;
	private static final int DEFAULT_HANDS = 35;
	private static final int DEFAULT_LEGS = 39;
	private static final int DEFAULT_FEET = 44;
	private static final int DEFAULT_BEARD = 14;
	private static final int DEFAULT_HAIR_COLOR = 7;
	private static final int DEFAULT_TORSO_COLOR = 8;
	private static final int DEFAULT_LEGS_COLOR = 9;
	private static final int DEFAULT_FEET_COLOR = 5;
	private static final int DEFAULT_SKIN_COLOR = 0;
	
	private int gender = DEFAULT_GENDER;
	private int head = DEFAULT_HEAD;
	private int torso = DEFAULT_TORSO;
	private int arms = DEFAULT_ARMS;
	private int hands = DEFAULT_HANDS;
	private int legs = DEFAULT_LEGS;
	private int feet = DEFAULT_FEET;
	private int beard = DEFAULT_BEARD;
	private int hairColor = DEFAULT_HAIR_COLOR;
	private int torsoColor = DEFAULT_TORSO_COLOR;
	private int legsColor = DEFAULT_LEGS_COLOR;
	private int feetColor = DEFAULT_FEET_COLOR;
	private int skinColor = DEFAULT_SKIN_COLOR;
	
	public int getGender() {
		return gender;
	}
	
	public int getHead() {
		return head;
	}
	
	public int getTorso() {
		return torso;
	}
	
	public int getArms() {
		return arms;
	}
	
	public int getHands() {
		return hands;
	}
	
	public int getLegs() {
		return legs;
	}
	
	public int getFeet() {
		return feet;
	}
	
	public int getBeard() {
		return beard;
	}
	
	public int getHairColor() {
		return hairColor;
	}
	
	public int getTorsoColor() {
		return torsoColor;
	}
	
	public int getLegsColor() {
		return legsColor;
	}
	
	public int getFeetColor() {
		return feetColor;
	}
	
	public int getSkinColor() {
		return skinColor;
	}
	
	public void setGender(int value) {
		gender = value;
	}
	
	public void setHead(int value) {
		head = value;
	}
	
	public void setTorso(int value) {
		torso = value;
	}
	
	public void setArms(int value) {
		arms = value;
	}
	
	public void setHands(int value) {
		hands = value;
	}
	
	public void setLegs(int value) {
		legs = value;
	}
	
	public void setFeet(int value) {
		feet = value;
	}
	
	public void setBeard(int value) {
		beard = value;
	}
	
	public void setHairColor(int value) {
		hairColor = value;
	}
	
	public void setTorsoColor(int value) {
		torsoColor = value;
	}
	
	public void setLegsColor(int value) {
		legsColor = value;
	}
	
	public void setFeetColor(int value) {
		feetColor = value;
	}
	
	public void setSkinColor(int value) {
		skinColor = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof Identikit) {
			Identikit idk = (Identikit) obj;
			return gender == idk.gender && head == idk.head &&
					torso == idk.torso && arms == idk.arms && 
					hands == idk.hands && legs == idk.legs &&
					feet == idk.feet && beard == idk.beard &&
					hairColor == idk.hairColor && torsoColor == idk.torsoColor &&
					legsColor == idk.legsColor && feetColor == idk.feetColor &&
					skinColor == idk.skinColor;
		} else {
			return false;
		}
	}
}
