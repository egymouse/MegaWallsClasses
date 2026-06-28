package org.ajls.megawallsclasses.inventory;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

public class LayoutResolver {

    private final LayoutRepository repository;

    public LayoutResolver(LayoutRepository repository) {
        this.repository = repository;
    }

    public InventoryLayout resolve(Player player, ClassKit kit) {
        InventoryLayout result = new InventoryLayout();

        InventoryLayout common = repository.loadCommonLayout(player);
        Optional<InventoryLayout> classCustomOpt = repository.loadClassLayout(player, kit.classId());

        InventoryLayout classDefault = getDefaultClassLayout(kit.classId());
        InventoryLayout commonDefault = DefaultLayouts.common();

        Set<Integer> usedSlots = new HashSet<>();

        for (ItemKey key : kit.itemKeys()) {
            OptionalInt slot =
                    getSlot(classCustomOpt.orElse(null), key)
                            .or(() -> getSlotOptional(common, key))
                            .or(() -> getSlotOptional(classDefault, key))
                            .or(() -> getSlotOptional(commonDefault, key));

            int finalSlot;

            if (slot.isPresent() && !usedSlots.contains(slot.getAsInt())) {
                finalSlot = slot.getAsInt();
            } else {
                finalSlot = findFirstFreeSlot(usedSlots);
            }

            result.setSlot(key, finalSlot);
            usedSlots.add(finalSlot);
        }

        return result;
    }

    private OptionalInt getSlot(InventoryLayout layout, ItemKey key) {
        if (layout == null) return OptionalInt.empty();
        return layout.getSlot(key);
    }

    private OptionalInt getSlotOptional(InventoryLayout layout, ItemKey key) {
        if (layout == null) return OptionalInt.empty();
        return layout.getSlot(key);
    }

    private int findFirstFreeSlot(Set<Integer> usedSlots) {
        for (int i = 0; i < 36; i++) {
            if (!usedSlots.contains(i)) return i;
        }
        throw new IllegalStateException("Inventory is full");
    }

    private InventoryLayout getDefaultClassLayout(int classId) {
        switch (classId) {
            case 13:
                return DefaultLayouts.snowman();
            default:
                return DefaultLayouts.common();
        }
    }
}
