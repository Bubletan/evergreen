package eg.game.model.item;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;

public final class ItemContainer implements Iterable<Item> {
    
    private int itemsCount;
    private final Item[] items;
    private final Type type;
    
    public static enum Type {
        
        STACK_SELECTIVELY, STACK_ALWAYS, STACK_NEVER
    }
    
    public ItemContainer(int capacity, Type type) {
        Preconditions.checkArgument(capacity >= 0, "Capacity may not be negative.");
        Preconditions.checkNotNull(type, "Type may not be null.");
        items = new Item[capacity];
        this.type = type;
    }
    
    public int usedSpace() {
        return itemsCount;
    }
    
    public int freeSpace() {
        return items.length - itemsCount;
    }
    
    public int totalSpace() {
        return items.length;
    }
    
    private boolean isStackable(ItemType type) {
        return this.type == Type.STACK_ALWAYS || this.type == Type.STACK_SELECTIVELY && type.isStackable();
    }
    
    private void checkItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item must not be null.");
        }
    }
    
    private void checkSlot(int slot) {
        if (slot < 0 || slot >= items.length) {
            throw new IndexOutOfBoundsException("Slot out of bounds: " + slot);
        }
    }
    
    /**
     * Counts the sum overflow for two non-negative integers.
     */
    private static int sumOverflow(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        if (a == Integer.MAX_VALUE) {
            return b;
        }
        if (b == Integer.MAX_VALUE) {
            return a;
        }
        if (Integer.MAX_VALUE - a >= b) {
            return 0;
        }
        return b - (Integer.MAX_VALUE - a);
    }
    
    /**
     * Checks if there is enough space for the specified {@link Item}.
     */
    public boolean canAdd(Item item) {
        checkItem(item);
        int quantity = item.getQuantity();
        if (quantity == 0) {
            return true;
        }
        ItemType type = item.getType();
        
        if (isStackable(type)) {
            
            int used = 0;
            for (Item it : items) {
                if (it == null) {
                    continue;
                }
                if (it.getType() == type) {
                    return sumOverflow(quantity, it.getQuantity()) == 0;
                }
                if (++used == itemsCount) {
                    break;
                }
            }
            return itemsCount != items.length;
            
        } else {
            
            return items.length - itemsCount >= quantity;
        }
    }
    
    /**
     * Checks if the specified {@link Item} exists and can be removed.
     */
    public boolean canRemove(Item item) {
        checkItem(item);
        int quantity = item.getQuantity();
        if (quantity == 0) {
            return true;
        }
        if (itemsCount == 0) {
            return false;
        }
        ItemType type = item.getType();
        
        if (isStackable(type)) {
            
            int used = 0;
            for (Item it : items) {
                if (it == null) {
                    continue;
                }
                if (it.getType() == type) {
                    return it.getQuantity() >= quantity;
                }
                if (++used == itemsCount) {
                    break;
                }
            }
            return false;
            
        } else {
            
            int count = 0;
            int used = 0;
            for (Item it : items) {
                if (it == null) {
                    continue;
                }
                if (it.getType() == type && ++count >= quantity) {
                    return true;
                }
                if (++used == itemsCount) {
                    break;
                }
            }
            return false;
            
        }
    }
    
    public Optional<Item> remove(Item item) {
        checkItem(item);
        int quantity = item.getQuantity();
        if (quantity == 0) {
            return Optional.of(item);
        }
        if (itemsCount == 0) {
            return Optional.empty();
        }
        ItemType type = item.getType();
        if (isStackable(type)) {
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
                            itemsCount--;
                            return Optional.of(it);
                        }
                    }
                    if (++used == itemsCount) {
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
                        itemsCount--;
                        if (++count == quantity) {
                            return Optional.of(item);
                        }
                    }
                    if (++used == itemsCount) {
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
        checkItem(item);
        ItemType type = item.getType();
        int quantity = item.getQuantity();
        if (isStackable(type)) {
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
                    if (++used == itemsCount) {
                        break;
                    }
                }
            }
            for (int i = 0, len = items.length; i < len; i++) {
                if (items[i] == null) {
                    items[i] = item;
                    itemsCount++;
                    return Optional.empty();
                }
            }
            return Optional.of(item);
        } else {
            if (itemsCount == items.length) {
                return Optional.of(item);
            }
            Item one = null;
            int count = 0;
            for (int i = 0, len = items.length; i < len; i++) {
                Item it = items[i];
                if (it == null) {
                    if (one == null) {
                        one = quantity == 1 ? item : new Item(type);
                    }
                    items[i] = one;
                    itemsCount++;
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
    
    public void swap(int a, int b) {
        checkSlot(a);
        checkSlot(b);
        if (a != b) {
            Item tmp = items[a];
            items[a] = items[b];
            items[b] = tmp;
        }
    }
    
    public void insert(int orig, int dest) {
        checkSlot(orig);
        checkSlot(dest);
        if (dest > orig) {
            for (int i = orig; i < dest; i++) {
                int i1 = i + 1;
                Item tmp = items[i];
                items[i] = items[i1];
                items[i1] = tmp;
            }
        } else if (orig > dest) {
            for (int i = orig; i > dest; i--) {
                int i1 = i - 1;
                Item tmp = items[i];
                items[i] = items[i1];
                items[i1] = tmp;
            }
        }
    }
    
    public void order() {
        int len = items.length;
        if (itemsCount == 0 || itemsCount == len) {
            return;
        }
        int pos = 0;
        for (; items[pos] != null; pos++); // iterate to first empty slot
        int i = pos + 1;
        for (; pos != itemsCount; i++) { // move the items
            Item tmp = items[i];
            if (tmp != null) {
                items[pos++] = tmp;
            }
        }
        for (; pos != i; pos++) { // clear the rest
            items[pos] = null;
        }
    }
    
    public Optional<Item> put(int slot, Item item) {
        checkSlot(slot);
        // TODO ensure the type
        Item tmp = items[slot];
        items[slot] = item;
        return Optional.ofNullable(tmp);
    }
    
    public Optional<Item> get(int slot) {
        checkSlot(slot);
        return Optional.ofNullable(items[slot]);
    }
    
    public Stream<Item> stream() {
        return Arrays.stream(items).filter(Objects::nonNull);
    }
    
    public Stream<Item> parallelStream() {
        return stream().parallel();
    }
    
    @Override
    public void forEach(Consumer<? super Item> action) {
        Objects.requireNonNull(action);
        stream().forEach(action);
    }
    
    @Override
    public Iterator<Item> iterator() {
        return stream().iterator();
    }
    
    @Override
    public Spliterator<Item> spliterator() {
        return stream().spliterator();
    }
}
