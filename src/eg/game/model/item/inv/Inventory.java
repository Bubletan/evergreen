package eg.game.model.item.inv;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import eg.game.model.item.Item;

public interface Inventory extends Iterable<Item> {
    
    /**
     * Returns the number of used slots.
     */
    public int usedSpace();
    
    /**
     * Returns the number of free slots.
     */
    public int freeSpace();
    
    /**
     * Returns the total number of slots.
     */
    public int totalSpace();
    
    /**
     * Returns {@code true} if there is no space free, otherwise {@code false}.<br>
     * <b>Note: This method does not take stackable items in count.</b>
     */
    public boolean isFull();
    
    /**
     * Returns {@code true} if there is no space used, otherwise {@code false}.
     */
    public boolean isEmpty();
    
    public OptionalInt findUsedSlot();
    
    public OptionalInt findFreeSlot();
    
    public boolean isUsedSlot(int slot);
    
    public boolean isFreeSlot(int slot);
    
    public void swapSlot(int oldSlot, int newSlot);
    
    public void insertSlot(int oldSlot, int newSlot);
    
    public void arrangeSlots();
    
    public void clearSlots();
    
    public Optional<Item> getSlot(int slot);
    
    public Optional<Item> clearSlot(int slot);
    
    public Optional<Item> setSlot(int slot, Item item);
    
    /**
     * Returns {@code true} if the item exists, otherwise {@code false}.
     */
    public boolean contains(Item item);
    
    /**
     * Returns {@code true} if all of the items exists, otherwise {@code false}.<br>
     * In case argument is an empty array, the result is {@code true}.
     */
    public boolean containsAll(Item... items);
    
    /**
     * Returns {@code true} if any of the items exists, otherwise {@code false}.<br>
     * In case argument is an empty array, the result is {@code false}.
     */
    public boolean containsAny(Item... items);
    
    /**
     * Returns {@code true} if space for the item exists, otherwise {@code false}.
     */
    public boolean spaceFor(Item item);
    
    /**
     * Returns {@code true} if space for all of the items exists, otherwise {@code false}.<br>
     * In case argument is an empty array, the result is {@code true}.
     */
    public boolean spaceForAll(Item... items);
    
    /**
     * Returns {@code true} if space for any of the items exists, otherwise {@code false}.<br>
     * In case argument is an empty array, the result is {@code false}.
     */
    public boolean spaceForAny(Item... items);
    
    /**
     * Adds the item if there is enough space.
     * Returns {@code true} if the addition was successful, otherwise {@code false}.
     */
    public boolean add(Item item);
    
    /**
     * Adds the item either fully or partially if there is not enough space.
     * Returns the overflow that could not be added.
     */
    public Optional<Item> addIgnoreOverflow(Item item);
    
    /**
     * Adds all of the items if there is enough space.
     * Returns {@code true} if the addition was successful, otherwise {@code false}.
     */
    public boolean addAll(Item... items);
    
    /**
     * Adds the first one of the items that there is space for.
     * Returns {@code true} if the addition was successful, otherwise {@code false}.
     */
    public boolean addAny(Item... items);
    
    /**
     * Removes the item if it exists.
     * Returns {@code true} if the removal was successful, otherwise {@code false}.
     */
    public boolean remove(Item item);
    
    /**
     * Removes the item either fully or partially if it does not completely exist.
     * Returns the underflow that could not be removed.
     */
    public Optional<Item> removeIgnoreUnderflow(Item item);
    
    /**
     * Removes all of the items if they all exist.
     * Returns {@code true} if the removal was successful, otherwise {@code false}.
     */
    public boolean removeAll(Item... items);
    
    /**
     * Removes the first one of the items that exists.
     * Returns {@code true} if the removal was successful, otherwise {@code false}.
     */
    public boolean removeAny(Item... items);
    
    public default Stream<Item> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
    
    public default Stream<Item> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }
}
