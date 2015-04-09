package eg.game.model.item;

import java.util.Optional;

import com.google.common.base.Preconditions;

public final class ItemContainer {
    
    private int capacity;
    private int size;
    private Item[] items;
    private Type type;
    
    public static enum Type {
        
        STACK_SELECTIVELY, STACK_ALWAYS, STACK_NEVER
    }
    
    public ItemContainer(int capacity, Type type) {
        Preconditions.checkArgument(capacity >= 0, "Capacity may not be negative.");
        Preconditions.checkNotNull(type, "Type may not be null.");
        this.capacity = capacity;
        items = new Item[capacity];
        this.type = type;
    }
    
    public boolean contains(Item item) {
        if (item == null) {
            return size != capacity;
        } else if (size == 0) {
            return false;
        }
        ItemType type = item.getType();
        int quantity = item.getQuantity();
        if (this.type == Type.STACK_ALWAYS || this.type == Type.STACK_SELECTIVELY && type.isStackable()) {
            int used = 0;
            for (Item it : items) {
                if (it != null) {
                    if (it.getType() == type) {
                        return it.getQuantity() >= quantity;
                    }
                    if (++used == size) {
                        break;
                    }
                }
            }
        } else {
            int count = 0;
            int used = 0;
            for (Item it : items) {
                if (it != null) {
                    if (it.getType() == type && ++count >= quantity) {
                        return true;
                    }
                    if (++used == size) {
                        break;
                    }
                }
            }
        }
        return false;
    }
    
    public Optional<Item> remove(Item item) {
        if (size == 0 || item == null) {
            return Optional.empty();
        }
        ItemType type = item.getType();
        int quantity = item.getQuantity();
        if (this.type == Type.STACK_ALWAYS
                || this.type == Type.STACK_SELECTIVELY && type.isStackable()) {
            int used = 0;
            for (int i = 0;; i++) {
                Item it = items[i];
                if (it != null) {
                    if (it.getType() == type) {
                        int qu = it.getQuantity();
                        if (qu > quantity) {
                            items[i] = new Item(type, qu - quantity);
                            return Optional.of(item);
                        } else {
                            items[i] = null;
                            size--;
                            return Optional.of(it);
                        }
                    }
                    if (++used == size) {
                        break;
                    }
                }
            }
            return Optional.empty();
        } else {
            int count = 0;
            int used = 0;
            for (int i = 0;; i++) {
                Item it = items[i];
                if (it != null) {
                    if (it.getType() == type) {
                        items[i] = null;
                        size--;
                        if (++count == quantity) {
                            return Optional.of(item);
                        }
                    }
                    if (++used == size) {
                        break;
                    }
                }
            }
            if (count == 0) {
                return Optional.empty();
            }
            return Optional.of(new Item(type, count));
        }
    }
    
    public Optional<Item> add(Item item) {
        if (item == null) {
            return Optional.empty();
        }
        ItemType type = item.getType();
        int quantity = item.getQuantity();
        if (this.type == Type.STACK_ALWAYS || this.type == Type.STACK_SELECTIVELY && type.isStackable()) {
            int used = 0;
            for (int i = 0;; i++) {
                Item it = items[i];
                if (it != null) {
                    if (it.getType() == type) {
                        int qu = it.getQuantity();
                        if (qu == Integer.MAX_VALUE) {
                            return Optional.of(item);
                        } else if (Integer.MAX_VALUE - qu >= quantity) {
                            items[i] = new Item(type, qu + quantity);
                            return Optional.empty();
                        } else {
                            items[i] = new Item(type, Integer.MAX_VALUE);
                            return Optional.of(new Item(type, quantity - (Integer.MAX_VALUE - qu)));
                        }
                    }
                    if (++used == size) {
                        break;
                    }
                }
            }
            for (int i = 0; i < capacity; i++) {
                if (items[i] == null) {
                    items[i] = item;
                    size++;
                    return Optional.empty();
                }
            }
            return Optional.of(item);
        } else {
            if (size == capacity) {
                return Optional.of(item);
            }
            Item one = null;
            int count = 0;
            for (int i = 0; i < capacity; i++) {
                Item it = items[i];
                if (it == null) {
                    if (one == null) {
                        one = quantity == 1 ? item : new Item(type);
                    }
                    items[i] = one;
                    size++;
                    if (++count == quantity) {
                        return Optional.empty();
                    }
                } else if (one == null && it.getType() == type) {
                    one = it;
                }
            }
            return Optional.of(new Item(type, quantity - count));
        }
    }
    
    public void swap(int slot1, int slot2) {
        Preconditions.checkElementIndex(slot1, capacity, "Slot one out of bounds: " + slot1);
        Preconditions.checkElementIndex(slot2, capacity, "Slot two out of bounds: " + slot2);
        if (slot1 != slot2) {
            Item tmp = items[slot1];
            items[slot1] = items[slot2];
            items[slot2] = tmp;
        }
    }
    
    public void insert(int slot1, int slot2) {
        Preconditions.checkElementIndex(slot1, capacity, "Slot one out of bounds: " + slot1);
        Preconditions.checkElementIndex(slot2, capacity, "Slot two out of bounds: " + slot2);
        if (slot2 > slot1) {
            for (int i = slot1; i < slot2; i++) {
                int i1 = i + 1;
                Item tmp = items[i];
                items[i] = items[i1];
                items[i1] = tmp;
            }
        } else if (slot1 > slot2) {
            for (int i = slot1; i > slot2; i--) {
                int i1 = i - 1;
                Item tmp = items[i];
                items[i] = items[i1];
                items[i1] = tmp;
            }
        }
    }
    
    public void set(int slot, Item item) {
        // TODO
    }
    
    public void get(int slot, Item item) {
        // TODO
    }
}
