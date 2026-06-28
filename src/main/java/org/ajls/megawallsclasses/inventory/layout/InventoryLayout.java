package org.ajls.megawallsclasses.inventory.layout;

import org.ajls.megawallsclasses.inventory.key.LayoutKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

public class InventoryLayout {

    private final Map<LayoutKey, Integer> slots = new HashMap<>();

    public InventoryLayout() {
    }

    public InventoryLayout(InventoryLayout other) {
        this.slots.putAll(other.slots);
    }

    public void setSlot(LayoutKey key, int slot) {
        if (slot < 0 || slot >= 36) {
            throw new IllegalArgumentException("slot must be 0-35: " + slot);
        }
        slots.put(key, slot);
    }

    public OptionalInt getSlot(LayoutKey key) {
        Integer slot = slots.get(key);
        return slot == null ? OptionalInt.empty() : OptionalInt.of(slot);
    }

    public boolean contains(LayoutKey key) {
        return slots.containsKey(key);
    }

    public Map<LayoutKey, Integer> asMap() {
        return Collections.unmodifiableMap(slots);
    }

    public InventoryLayout copy() {
        return new InventoryLayout(this);
    }
}
