package eg.game.event.npc;

import eg.game.event.Event;
import eg.game.model.npc.Npc;

public abstract class NpcEvent implements Event {
    
    private final Npc author;
    
    public NpcEvent(Npc author) {
        this.author = author;
    }
    
    public Npc getAuthor() {
        return author;
    }
}
