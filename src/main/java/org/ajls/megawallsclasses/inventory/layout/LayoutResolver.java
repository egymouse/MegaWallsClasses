package org.ajls.megawallsclasses.inventory.layout;

import org.ajls.megawallsclasses.customclasses.AbstractCustomClass;
import org.ajls.megawallsclasses.inventory.key.LayoutKey;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

public class LayoutResolver {
    //用来根据玩家职业加载顺序的

    private final LayoutRepository repository;

    public LayoutResolver(LayoutRepository repository) {
        this.repository = repository;
    }

    public InventoryLayout resolve(Player player, AbstractCustomClass customClass) {
        InventoryLayout result = new InventoryLayout();

        InventoryLayout commonPlayerLayout = repository.loadCommonLayout(player);
        Optional<InventoryLayout> classPlayerLayoutOpt =
                repository.loadClassLayout(player, customClass.getClassEnum().classId);

        InventoryLayout classDefaultLayout = ClassDefaultLayoutResolver.getDefaultLayout(customClass);
        InventoryLayout commonDefaultLayout = DefaultLayouts.common();

        Set<Integer> usedSlots = new HashSet<>();

        for (LayoutKey key : customClass.getDefaultLayoutKeys()) {
            OptionalInt slotOpt = firstPresent(
                    classPlayerLayoutOpt.map(layout -> layout.getSlot(key)).orElse(OptionalInt.empty()),
                    commonPlayerLayout.getSlot(key),
                    classDefaultLayout.getSlot(key),
                    commonDefaultLayout.getSlot(key)
            );

            int finalSlot;

            if (slotOpt.isPresent() && isValidSlot(slotOpt.getAsInt()) && !usedSlots.contains(slotOpt.getAsInt())) {
                finalSlot = slotOpt.getAsInt();
            } else {
                finalSlot = findFirstFreeSlot(usedSlots);
            }

            result.setSlot(key, finalSlot);
            usedSlots.add(finalSlot);
        }

        return result;
    }

    private OptionalInt firstPresent(OptionalInt... options) {
        for (OptionalInt option : options) {
            if (option != null && option.isPresent()) {
                return option;
            }
        }
        return OptionalInt.empty();
    }

    private boolean isValidSlot(int slot) {
        return slot >= 0 && slot < 36;
    }

    private int findFirstFreeSlot(Set<Integer> usedSlots) {
        for (int i = 0; i < 36; i++) {
            if (!usedSlots.contains(i)) {
                return i;
            }
        }

        throw new IllegalStateException("No free slot");
    }
}
