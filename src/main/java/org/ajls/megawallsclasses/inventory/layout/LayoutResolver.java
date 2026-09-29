package org.ajls.megawallsclasses.inventory.layout;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.ClassEnum;
import org.ajls.megawallsclasses.NameSpacedKeys;
import org.ajls.megawallsclasses.customclasses.AbstractCustomClass;
import org.ajls.megawallsclasses.inventory.key.LayoutKey;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

public class LayoutResolver {
    //用来根据玩家职业加载顺序的

    public static InventoryLayout resolve(Player player, AbstractCustomClass customClass) {

        if (customClass == ClassEnum.NONE.customClass) {
            return LayoutRepository.loadCommonLayout(player);
        }
        return LayoutRepository.loadClassLayout(player, customClass.getClassEnum().classId);
    }

    public static InventoryLayout toLayout(Inventory inventory) {
        InventoryLayout inventoryLayout = new InventoryLayout();
        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack != null) {
                if (itemStack.getType() != Material.AIR) {
                    String itemType = ItemStackU.getStringPersistentData(itemStack, NameSpacedKeys.ITEM_TYPE);
                    LayoutKey layoutKey = LayoutKey.fromId(itemType);
                    inventoryLayout.setSlot(layoutKey, i);
                }
            }
        }
        return inventoryLayout;
    }


    private static OptionalInt firstPresent(OptionalInt... options) {
        for (OptionalInt option : options) {
            if (option != null && option.isPresent()) {
                return option;
            }
        }
        return OptionalInt.empty();
    }

    private static boolean isValidSlot(int slot) {
        return slot >= 0 && slot < 36;
    }

    private static int findFirstFreeSlot(Set<Integer> usedSlots) {
        for (int i = 0; i < 36; i++) {
            if (!usedSlots.contains(i)) {
                return i;
            }
        }

        throw new IllegalStateException("No free slot");
    }
}
