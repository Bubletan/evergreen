package eg.game.model.player;

import java.util.Arrays;

public final class Statistics {
    
    private static final int SKILL_COUNT = 21;
    
    private static final int ATTACK = 0;
    private static final int DEFENCE = 1;
    private static final int STRENGTH = 2;
    private static final int HITPOINTS = 3;
    private static final int RANGED = 4;
    private static final int PRAYER = 5;
    private static final int MAGIC = 6;
    private static final int COOKING = 7;
    private static final int WOODCUTTING = 8;
    private static final int FLETCHING = 9;
    private static final int FISHING = 10;
    private static final int FIREMAKING = 11;
    private static final int CRAFTING = 12;
    private static final int SMITHING = 13;
    private static final int MINING = 14;
    private static final int HERBLORE = 15;
    private static final int AGILITY = 16;
    private static final int THIEVING = 17;
    private static final int SLAYER = 18;
    private static final int FARMING = 19;
    private static final int RUNECRAFTING = 20;
    
    private final Skill[] skills = new Skill[SKILL_COUNT];
    
    public Statistics() {
        for (int i = 0; i < SKILL_COUNT; i++) {
            skills[i] = new Skill();
        }
        Skill hitpoints = getHitpoints();
        hitpoints.setExperience10(1184_0);
        hitpoints.setPseudoLevel(10);
    }
    
    public int getCombatLevel() {
        
        int magic = getMagic().getLevel();
        int ranged = getRanged().getLevel();
        int attstr = getAttack().getLevel() + getStrength().getLevel();
        int defence = getDefence().getLevel();
        int prayer = getPrayer().getLevel();
        int hitpoints = getHitpoints().getLevel();
        
        int result = (hitpoints + defence << 14) + (prayer << 13);
        if (ranged * 3 >> 1 > attstr) {
            result += (ranged << 15) - (ranged << 13);
        } else if (magic * 3 >> 1 > attstr) {
            result += (magic << 15) - (magic << 13);
        } else {
            result += (attstr << 14) + (attstr << 13);
        }
        return result >> 16;
    }
    
    public int getTotalLevel() {
        return Arrays.stream(skills).mapToInt(s -> s.getLevel()).sum();
    }
    
    public long getTotalExperience() {
        return Arrays.stream(skills).mapToLong(s -> s.getExperience()).sum();
    }
    
    public Skill getAttack() {
        return skills[ATTACK];
    }
    
    public Skill getDefence() {
        return skills[DEFENCE];
    }
    
    public Skill getStrength() {
        return skills[STRENGTH];
    }
    
    public Skill getHitpoints() {
        return skills[HITPOINTS];
    }
    
    public Skill getRanged() {
        return skills[RANGED];
    }
    
    public Skill getPrayer() {
        return skills[PRAYER];
    }
    
    public Skill getMagic() {
        return skills[MAGIC];
    }
    
    public Skill getCooking() {
        return skills[COOKING];
    }
    
    public Skill getWoodcutting() {
        return skills[WOODCUTTING];
    }
    
    public Skill getFletching() {
        return skills[FLETCHING];
    }
    
    public Skill getFishing() {
        return skills[FISHING];
    }
    
    public Skill getFiremaking() {
        return skills[FIREMAKING];
    }
    
    public Skill getCrafting() {
        return skills[CRAFTING];
    }
    
    public Skill getSmithing() {
        return skills[SMITHING];
    }
    
    public Skill getMining() {
        return skills[MINING];
    }
    
    public Skill getHerblore() {
        return skills[HERBLORE];
    }
    
    public Skill getAgility() {
        return skills[AGILITY];
    }
    
    public Skill getThieving() {
        return skills[THIEVING];
    }
    
    public Skill getSlayer() {
        return skills[SLAYER];
    }
    
    public Skill getFarming() {
        return skills[FARMING];
    }
    
    public Skill getRunecrafting() {
        return skills[RUNECRAFTING];
    }
}
