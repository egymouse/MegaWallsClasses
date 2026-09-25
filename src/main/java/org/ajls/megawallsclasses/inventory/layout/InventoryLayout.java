package org.ajls.megawallsclasses.inventory.layout;

import org.ajls.megawallsclasses.inventory.key.ItemCount;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.ajls.megawallsclasses.inventory.key.LayoutKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

public class InventoryLayout {

    private final Map<Integer, ItemCount> slots = new HashMap<>();

    public InventoryLayout() {
    }

    public InventoryLayout(InventoryLayout other) {
        this.slots.putAll(other.slots);
    }

    public void setSlot(LayoutKey key, int slot) {
        setSlot(slot, key.kind(), -1);
    }


    public void setSlot(int slot, ItemKind itemKind, int count) {
        if (slot < 0 || slot >= 36) {
            throw new IllegalArgumentException("slot must be 0-35: " + slot);
        }
        slots.put(slot, new ItemCount(itemKind, count));
    }

    public void setSlot(int slot, ItemKind itemKind) {
        setSlot(slot, itemKind, -1);
    }

    public void removeSlot(int slot) {
        slots.remove(slot);
    }

    public ItemCount getSlot(int slot) {
        return slots.get(slot);
    }

    public Map<Integer, ItemCount> asMap() {
        return Collections.unmodifiableMap(slots);
    }

    public InventoryLayout copy() {
        return new InventoryLayout(this);
    }
}
