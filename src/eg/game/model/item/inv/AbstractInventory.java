package eg.game.model.item.inv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

import eg.game.model.item.Item;
import eg.game.model.item.ItemType;

public abstract class AbstractInventory implements Inventory {
    
    private int itemsCount;
    private final Item[] items;
    
    public AbstractInventory(int space) {
        items = new Item[space];
    }
    
    protected abstract boolean isStackable(ItemType type);
    
    @Override
    public int usedSpace() {
        return itemsCount;
    }
    
    @Override
    public int freeSpace() {
        return items.length - itemsCount;
    }
    
    @Override
    public int totalSpace() {
        return items.length;
    }
    
    @Override
    public boolean isFull() {
        return itemsCount == items.length;
    }
    
    @Override
    public boolean isEmpty() {
        return itemsCount == 0;
    }
    
    @Override
    public OptionalInt findUsedSlot() {
        if (isEmpty()) {
            return OptionalInt.empty();
        }
        for (int i = 0;; i++) {
            if (items[i] != null) {
                return OptionalInt.of(i);
            }
        }
    }
    
    @Override
    public OptionalInt findFreeSlot() {
        if (isFull()) {
            return OptionalInt.empty();
        }
        for (int i = 0;; i++) {
            if (items[i] == null) {
                return OptionalInt.of(i);
            }
        }
    }
    
    @Override
    public boolean isUsedSlot(int slot) {
        checkSlot(slot);
        return items[slot] != null;
    }
    
    @Override
    public boolean isFreeSlot(int slot) {
        checkSlot(slot);
        return items[slot] == null;
    }
    
    @Override
    public void swapSlot(int oldSlot, int newSlot) {
        checkSlot(oldSlot);
        checkSlot(newSlot);
        if (oldSlot != newSlot) {
            Item tmp = items[oldSlot];
            items[oldSlot] = items[newSlot];
            items[newSlot] = tmp;
        }
    }
    
    @Override
    public void insertSlot(int oldSlot, int newSlot) {
        if (Math.abs(newSlot - oldSlot) <= 1) {
            swapSlot(oldSlot, newSlot);
            return;
        }
        checkSlot(oldSlot);
        checkSlot(newSlot);
        Item item = items[oldSlot];
        if (oldSlot < newSlot) {
            System.arraycopy(items, oldSlot + 1, items, oldSlot, newSlot - oldSlot);
        } else {
            System.arraycopy(items, newSlot, items, newSlot + 1, oldSlot - newSlot);
        }
        items[newSlot] = item;
    }
    
    @Override
    public void arrangeSlots() {
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
    
    @Override
    public void clearSlots() {
        Arrays.fill(items, null);
        itemsCount = 0;
    }
    
    @Override
    public Optional<Item> getSlot(int slot) {
        checkSlot(slot);
        return Optional.ofNullable(items[slot]);
    }
    
    @Override
    public Optional<Item> clearSlot(int slot) {
        checkSlot(slot);
        Optional<Item> old = Optional.ofNullable(items[slot]);
        items[slot] = null;
        return old;
    }
    
    @Override
    public Optional<Item> setSlot(int slot, Item item) {
        checkSlot(slot);
        checkItem(item);
        Optional<Item> old = Optional.ofNullable(items[slot]);
        items[slot] = item;
        return old;
    }
    
    @Override
    public boolean contains(Item item) {
        checkItem(item);
        int quantity = item.getQuantity();
        if (quantity == 0) {
            return true;
        }
        if (isEmpty()) {
            return false;
        }
        ItemType type = item.getType();
        if (isStackable(type)) {
            OptionalInt slot = findSlotOfType(type);
            return slot.isPresent() && items[slot.getAsInt()].getQuantity() >= quantity;
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
    
    @Override
    public boolean containsAll(Item... items) {
        Objects.requireNonNull(items);
        if (items.length == 0) {
            return true;
        }
        if (items.length == 1) {
            return contains(items[0]);
        }
        for (Item item : items) {
            checkItem(item);
        }
        List<ItemType> duplicates = new ArrayList<>();
        outer:
        for (int i = 0, len = items.length; i < len; i++) {
            Item item = items[i];
            ItemType type = item.getType();
            if (duplicates.contains(type)) {
                continue;
            }
            for (int j = 0; j < len; j++) {
                if (i == j) {
                    continue;
                }
                if (type == items[j].getType()) {
                    duplicates.add(type);
                    continue outer;
                }
            }
            if (!contains(item)) {
                return false;
            }
        }
        if (duplicates.isEmpty()) {
            return true;
        }
        for (ItemType type : duplicates) {
            int quantity = 0;
            for (Item item : items) {
                if (type != item.getType()) {
                    continue;
                }
                quantity += item.getQuantity();
            }
            if (!contains(new Item(type, quantity))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean containsAny(Item... items) {
        Objects.requireNonNull(items);
        if (items.length == 0) {
            return false;
        }
        if (items.length == 1) {
            return contains(items[0]);
        }
        return Arrays.stream(items).anyMatch(this::contains);
    }
    
    @Override
    public boolean spaceFor(Item item) {
        checkItem(item);
        int quantity = item.getQuantity();
        if (quantity == 0) {
            return true;
        }
        ItemType type = item.getType();
        if (isStackable(type)) {
            OptionalInt slot = findSlotOfType(type);
            return slot.isPresent() ? quantity + items[slot.getAsInt()].getQuantity() >= 0 : !isFull();
        } else {
            return freeSpace() >= quantity;
        }
    }
    
    @Override
    public boolean spaceForAll(Item... items) {
        Objects.requireNonNull(items);
        if (items.length == 0) {
            return true;
        }
        if (items.length == 1) {
            return spaceFor(items[0]);
        }
        int free = freeSpace();
        List<Item> stackables = new ArrayList<>();
        List<ItemType> duplicates = new ArrayList<>();
        for (Item item : items) {
            checkItem(item);
            int quantity = item.getQuantity();
            if (quantity == 0) {
                continue;
            }
            ItemType type = item.getType();
            if (isStackable(type)) {
                if (!duplicates.contains(type)) {
                    for (Item it : stackables) {
                        if (type != it.getType()) {
                            continue;
                        }
                        duplicates.add(type);
                        break;
                    }
                }
                stackables.add(item);
                continue;
            }
            if ((free -= quantity) < 0) {
                return false;
            }
        }
        if (stackables.isEmpty()) {
            return true;
        }
        if (!duplicates.isEmpty()) {
            for (ItemType type : duplicates) {
                int quantity = 0;
                for (Iterator<Item> it = stackables.iterator(); it.hasNext();) {
                    Item item = it.next();
                    if (item.getType() != type) {
                        continue;
                    }
                    quantity += item.getQuantity();
                    if (quantity < 0) {
                        return false;
                    }
                    it.remove();
                }
                stackables.add(new Item(type, quantity));
            }
        }
        for (Item item : stackables) {
            OptionalInt slot = findSlotOfType(item.getType());
            if (slot.isPresent()) {
                if (items[slot.getAsInt()].getQuantity() + item.getQuantity() < 0) {
                    return false;
                }
            } else {
                if (--free < 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean spaceForAny(Item... items) {
        Objects.requireNonNull(items);
        if (items.length == 0) {
            return false;
        }
        if (items.length == 1) {
            return spaceFor(items[0]);
        }
        return Arrays.stream(items).anyMatch(this::spaceFor);
    }
    
    @Override
    public boolean add(Item item) {
        checkItem(item);
        int quantity = item.getQuantity();
        if (quantity == 0) {
            return true;
        }
        ItemType type = item.getType();
        if (isStackable(type)) {
            OptionalInt slotOfType = findSlotOfType(type);
            if (slotOfType.isPresent()) {
                int slot = slotOfType.getAsInt();
                int newQuantity = quantity + items[slot].getQuantity();
                if (newQuantity >= 0) {
                    items[slot] = new Item(type, newQuantity);
                    return true;
                } else {
                    return false;
                }
            }
            OptionalInt freeSlot = findFreeSlot();
            if (freeSlot.isPresent()) {
                items[freeSlot.getAsInt()] = item;
                itemsCount++;
                return true;
            } else {
                return false;
            }
        } else {
            if (freeSpace() < quantity) {
                return false;
            }
            Item one = null;
            int count = 0;
            for (int i = 0;; i++) {
                Item it = items[i];
                if (it == null) {
                    if (one == null) {
                        one = quantity == 1 ? item : new Item(type);
                    }
                    items[i] = one;
                    itemsCount++;
                    if (++count == quantity) {
                        return true;
                    }
                } else if (one == null && it.getType() == type) {
                    one = it;
                }
            }
        }
    }
    
    @Override
    public Optional<Item> addIgnoreOverflow(Item item) {
        checkItem(item);
        int quantity = item.getQuantity();
        if (quantity == 0) {
            return Optional.empty();
        }
        ItemType type = item.getType();
        if (isStackable(type)) {
            OptionalInt slotOfType = findSlotOfType(type);
            if (slotOfType.isPresent()) {
                int slot = slotOfType.getAsInt();
                int oldQuantity = items[slot].getQuantity();
                if (oldQuantity == Integer.MAX_VALUE) {
                    return Optional.of(item);
                }
                int newQuantity = quantity + oldQuantity;
                if (newQuantity >= 0) {
                    items[slot] = new Item(type, newQuantity);
                    return Optional.empty();
                } else {
                    items[slot] = new Item(type, Integer.MAX_VALUE);
                    return Optional.of(new Item(type, newQuantity - Integer.MAX_VALUE));
                }
            }
            OptionalInt freeSlot = findFreeSlot();
            if (freeSlot.isPresent()) {
                items[freeSlot.getAsInt()] = item;
                itemsCount++;
                return Optional.empty();
            } else {
                return Optional.of(item);
            }
        } else {
            if (isFull()) {
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
    
    @Override
    public boolean addAll(Item... items) {
        Objects.requireNonNull(items);
        if (spaceForAll(items)) {
            for (Item item : items) {
                add(item);
            }
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean addAny(Item... items) {
        Objects.requireNonNull(items);
        for (Item item : items) {
            if (add(item)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean remove(Item item) {
        checkItem(item);
        int quantity = item.getQuantity();
        if (quantity == 0) {
            return true;
        }
        if (isEmpty()) {
            return false;
        }
        ItemType type = item.getType();
        if (isStackable(type)) {
            OptionalInt slotOfType = findSlotOfType(type);
            if (!slotOfType.isPresent()) {
                return false;
            }
            int slot = slotOfType.getAsInt();
            int oldQuantity = items[slot].getQuantity();
            if (quantity < oldQuantity) {
                int newQuantity = oldQuantity - quantity;
                items[slot] = new Item(type, newQuantity);
                return true;
            } else if (oldQuantity == quantity) {
                items[slot] = null;
                itemsCount--;
                return true;
            } else {
                return false;
            }
        } else {
            if (quantity > countQuantityOfType(type)) {
                return false;
            }
            int count = 0;
            for (int i = 0;; i++) {
                Item it = items[i];
                if (it == null || it.getType() != type) {
                    continue;
                }
                items[i] = null;
                if (++count == quantity) {
                    itemsCount -= quantity;
                    return true;
                }
            }
        }
    
    }
    
    @Override
    public Optional<Item> removeIgnoreUnderflow(Item item) {
        checkItem(item);
        int quantity = item.getQuantity();
        if (quantity == 0) {
            return Optional.of(item);
        }
        if (isEmpty()) {
            return Optional.empty();
        }
        ItemType type = item.getType();
        if (isStackable(type)) {
            OptionalInt slotOfType = findSlotOfType(type);
            if (!slotOfType.isPresent()) {
                return Optional.of(item);
            }
            int slot = slotOfType.getAsInt();
            int oldQuantity = items[slot].getQuantity();
            if (quantity < oldQuantity) {
                int newQuantity = oldQuantity - quantity;
                items[slot] = new Item(type, newQuantity);
                return Optional.empty();
            } else if (oldQuantity == quantity) {
                items[slot] = null;
                itemsCount--;
                return Optional.empty();
            } else {
                items[slot] = null;
                itemsCount--;
                return Optional.of(new Item(type, quantity - oldQuantity));
            }
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
    
    @Override
    public boolean removeAll(Item... items) {
        Objects.requireNonNull(items);
        if (containsAll(items)) {
            for (Item item : items) {
                remove(item);
            }
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean removeAny(Item... items) {
        Objects.requireNonNull(items);
        for (Item item : items) {
            if (remove(item)) {
                return true;
            }
        }
        return false;
    }
    
    private int countQuantityOfType(ItemType type) {
        if (isStackable(type)) {
            OptionalInt slot = findSlotOfType(type);
            return slot.isPresent() ? items[slot.getAsInt()].getQuantity() : 0;
        } else {
            int used = 0;
            int count = 0;
            for (Item item : items) {
                if (item == null) {
                    continue;
                }
                if (item.getType() == type) {
                    count++;
                }
                if (++used == itemsCount) {
                    break;
                }
            }
            return count;
        }
    }
    
    private int countSpaceForType(ItemType type) {
        if (isStackable(type)) {
            OptionalInt slot = findSlotOfType(type);
            if (slot.isPresent()) {
                return Integer.MAX_VALUE - items[slot.getAsInt()].getQuantity();
            }
            return isFull() ? 0 : Integer.MAX_VALUE;
        } else {
            return freeSpace();
        }
    }
    
    private OptionalInt findSlotOfType(ItemType type) {
        int used = 0;
        for (int i = 0; used != itemsCount; i++) {
            if (items[i].getType() == type) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }
    
    private boolean isSlotOfType(int slot, ItemType type) {
        checkSlot(slot);
        Item item = items[slot];
        return item != null && item.getType() == type;
    }
    
    private void checkSlot(int slot) {
        if (slot < 0 || slot >= items.length) {
            throw new IndexOutOfBoundsException("Slot out of bounds: " + slot);
        }
    }
    
    private void checkItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item must not be null.");
        }
    }
    
    @Override
    public Stream<Item> stream() {
        return Arrays.stream(items).filter(Objects::nonNull);
    }
    
    @Override
    public Stream<Item> parallelStream() {
        return stream().parallel();
    }
    
    @Override
    public void forEach(Consumer<? super Item> action) {
        Objects.requireNonNull(action);
        for (Item item : items) {
            if (item == null) {
                continue;
            }
            action.accept(item);
        }
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
