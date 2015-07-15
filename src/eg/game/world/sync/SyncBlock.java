package eg.game.world.sync;

import eg.game.model.MobileEntity;
import eg.game.model.Hit;
import eg.game.model.npc.NpcType;
import eg.game.model.player.Player;
import eg.game.world.Coordinate;

/**
 * @author Bubletan <https://github.com/Bubletan>
 */
public abstract class SyncBlock {
    
    private final Type type;
    
    private SyncBlock(Type type) {
        this.type = type;
    }
    
    public Type getType() {
        return type;
    }
    
    public static enum Type {
        
        FORCE_MOVEMENT, EFFECT, ANIMATION, FORCE_CHAT_MESSAGE, CHAT_MESSAGE,
        INTERACT, APPEARANCE, TURN, PRIMARY_HIT, SECONDARY_HIT, TRANSFORM
    }
    
    public static final class ForceMovement extends SyncBlock {
        
        private final eg.game.model.ForceMovement forceMovement;
        
        public ForceMovement(eg.game.model.ForceMovement forceMovement) {
            super(Type.FORCE_MOVEMENT);
            this.forceMovement = forceMovement;
        }
        
        public eg.game.model.ForceMovement getForceMovement() {
            return forceMovement;
        }
    }
    
    public static final class Effect extends SyncBlock {
        
        private final eg.game.model.Effect effect;
        
        public Effect(eg.game.model.Effect effect) {
            super(Type.EFFECT);
            this.effect = effect;
        }
        
        public eg.game.model.Effect getEffect() {
            return effect;
        }
    }
    
    public static final class Animation extends SyncBlock {
        
        private final eg.game.model.Animation animation;
        
        public Animation(eg.game.model.Animation animation) {
            super(Type.ANIMATION);
            this.animation = animation;
        }
        
        public eg.game.model.Animation getAnimation() {
            return animation;
        }
    }
    
    public static final class ForceChatMessage extends SyncBlock {
        
        private final eg.game.model.ForceChatMessage forceChatMessage;
        
        public ForceChatMessage(eg.game.model.ForceChatMessage forceChatMessage) {
            super(Type.FORCE_CHAT_MESSAGE);
            this.forceChatMessage = forceChatMessage;
        }
        
        public eg.game.model.ForceChatMessage getForceChatMessage() {
            return forceChatMessage;
        }
    }
    
    public static final class ChatMessage extends SyncBlock {
        
        private final byte[] compressedMessage;
        private final int colorEffect;
        private final int animationEffect;
        private final int privilege;
        
        public ChatMessage(byte[] compressedMessage, int colorEffect, int animationEffect, int privilege) {
            super(Type.CHAT_MESSAGE);
            this.compressedMessage = compressedMessage;
            this.colorEffect = colorEffect;
            this.animationEffect = animationEffect;
            this.privilege = privilege;
        }
        
        public byte[] getCompressedMessage() {
            return compressedMessage;
        }
        
        public int getColorEffect() {
            return colorEffect;
        }
        
        public int getAnimationEffect() {
            return animationEffect;
        }
        
        public int getPrivilege() {
            return privilege;
        }
    }
    
    public static final class Interact extends SyncBlock {
        
        private final MobileEntity target;
        
        public Interact(MobileEntity target) {
            super(Type.INTERACT);
            this.target = target;
        }
        
        public MobileEntity getTarget() {
            return target;
        }
    }
    
    // TODO change this (temporary solution)
    public static final class Appearance extends SyncBlock {
        
        private final Player player;
        
        public Appearance(Player player) {
            super(Type.APPEARANCE);
            this.player = player;
        }
        
        public Player getPlayer() {
            return player;
        }
    }
    
    public static final class Turn extends SyncBlock {
        
        private final Coordinate target;
        
        public Turn(Coordinate target) {
            super(Type.TURN);
            this.target = target;
        }
        
        public Coordinate getTarget() {
            return target;
        }
    }
    
    public static final class PrimaryHit extends SyncBlock {
        
        private final Hit hit;
        private int healthLeft;
        private int healthTotal;
        
        public PrimaryHit(Hit hit, int healthLeft, int healthTotal) {
            super(Type.PRIMARY_HIT);
            this.hit = hit;
            this.healthLeft = healthLeft;
            this.healthTotal = healthTotal;
        }
        
        public Hit getHit() {
            return hit;
        }
        
        public int getHealthLeft() {
            return healthLeft;
        }
        
        public int getHealthTotal() {
            return healthTotal;
        }
    }
    
    public static final class SecondaryHit extends SyncBlock {
        
        private final Hit hit;
        private int healthLeft;
        private int healthTotal;
        
        public SecondaryHit(Hit hit, int healthLeft, int healthTotal) {
            super(Type.SECONDARY_HIT);
            this.hit = hit;
            this.healthLeft = healthLeft;
            this.healthTotal = healthTotal;
        }
        
        public Hit getHit() {
            return hit;
        }
        
        public int getHealthLeft() {
            return healthLeft;
        }
        
        public int getHealthTotal() {
            return healthTotal;
        }
    }
    
    public static final class Transform extends SyncBlock {
        
        private final NpcType npcType;
        
        public Transform(NpcType npcType) {
            super(Type.TRANSFORM);
            this.npcType = npcType;
        }
        
        public NpcType getNpcType() {
            return npcType;
        }
    }
}
