package org.ajls.megawallsclasses.inventory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

public class InventoryLayout {

    private final Map<ItemKey, Integer> slots = new HashMap<>();

    public void setSlot(ItemKey key, int slot) {
        if (slot < 0 || slot >= 36) {
            throw new IllegalArgumentException("slot must be 0-35");
        }
        slots.put(key, slot);
    }

    public OptionalInt getSlot(ItemKey key) {
        Integer slot = slots.get(key);
        return slot == null ? OptionalInt.empty() : OptionalInt.of(slot);
    }

    public Map<ItemKey, Integer> asMap() {
        return Collections.unmodifiableMap(slots);
    }

    public boolean contains(ItemKey key) {
        return slots.containsKey(key);
    }
}
