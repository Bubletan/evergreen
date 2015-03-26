package eg.game.world.sync;

import eg.game.model.npc.NpcType;
import eg.game.world.Coordinate;
import eg.game.world.Direction;

public abstract class SyncStatus {
    
    private final Type type;
    
    private SyncStatus(Type type) {
        this.type = type;
    }
    
    public Type getType() {
        return type;
    }
    
    public static enum Type {
        
        TRANSITION, RUN, WALK, STAND, PLAYER_ADDITION, REMOVAL, NPC_ADDITION
    }
    
    public static final class Transition extends SyncStatus {
        
        private final Coordinate coordinate;
        private final boolean sectorChanging;
        
        public Transition(Coordinate coordinate, boolean sectorChanging) {
            super(Type.TRANSITION);
            this.coordinate = coordinate;
            this.sectorChanging = sectorChanging;
        }
        
        public Coordinate getCoordinate() {
            return coordinate;
        }
        
        public boolean isSectorChanging() {
            return sectorChanging;
        }
    }
    
    public static final class Run extends SyncStatus {
        
        private final Direction primaryDirection;
        private final Direction secondaryDirection;
        
        public Run(Direction primaryDirection, Direction secondaryDirection) {
            super(Type.RUN);
            this.primaryDirection = primaryDirection;
            this.secondaryDirection = secondaryDirection;
        }
        
        public Direction getPrimaryDirection() {
            return primaryDirection;
        }
        
        public Direction getSecondaryDirection() {
            return secondaryDirection;
        }
    }
    
    public static final class Walk extends SyncStatus {
        
        private final Direction direction;
        
        public Walk(Direction direction) {
            super(Type.WALK);
            this.direction = direction;
        }
        
        public Direction getDirection() {
            return direction;
        }
    }
    
    public static final class Stand extends SyncStatus {
        
        public Stand() {
            super(Type.STAND);
        }
    }
    
    public static final class PlayerAddition extends SyncStatus {
        
        private final int index;
        private final Coordinate coordinate;
        
        public PlayerAddition(int index, Coordinate coordinate) {
            super(Type.PLAYER_ADDITION);
            this.index = index;
            this.coordinate = coordinate;
        }
        
        public int getIndex() {
            return index;
        }
        
        public Coordinate getCoordinate() {
            return coordinate;
        }
    }
    
    public static final class NpcAddition extends SyncStatus {
        
        private final int index;
        private final Coordinate coordinate;
        private final NpcType npcType;
        
        public NpcAddition(int index, Coordinate coordinate, NpcType npcType) {
            super(Type.NPC_ADDITION);
            this.index = index;
            this.coordinate = coordinate;
            this.npcType = npcType;
        }
        
        public int getIndex() {
            return index;
        }
        
        public Coordinate getCoordinate() {
            return coordinate;
        }
        
        public NpcType getNpcType() {
            return npcType;
        }
    }
    
    public static final class Removal extends SyncStatus {
        
        public Removal() {
            super(Type.REMOVAL);
        }
    }
}
