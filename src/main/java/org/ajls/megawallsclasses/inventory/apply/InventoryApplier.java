package org.ajls.megawallsclasses.inventory.apply;

import org.ajls.megawallsclasses.ClassEnum;
import org.ajls.megawallsclasses.customclasses.AbstractCustomClass;
import org.ajls.megawallsclasses.inventory.key.LayoutKey;
import org.ajls.megawallsclasses.inventory.layout.InventoryLayout;
import org.ajls.megawallsclasses.inventory.layout.LayoutResolver;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.OptionalInt;

public class InventoryApplier {

//    private final LayoutResolver layoutResolver;
//
//    public InventoryApplier(LayoutResolver layoutResolver) {
//        this.layoutResolver = layoutResolver;
//    }

    public static void apply(Player player) {
        AbstractCustomClass customClass = ClassEnum.getCustomClassByPlayer(player);
        apply(player, customClass);
    }

    public static void apply(Player player, AbstractCustomClass customClass) {
        clearStorageContents(player);

        InventoryLayout layout = LayoutResolver.resolve(player, customClass);
        layout.asMap().forEach((key, value) -> {
            player.getInventory().setItem(value, customClass.createInventoryItem(key));
        });

//        for (LayoutKey key : customClass.getDefaultLayoutKeys()) {
//            OptionalInt slotOpt = layout.getSlot(key);
//            if (!slotOpt.isPresent()) continue;
//
//            ItemStack item = customClass.createInventoryItem(player, key);
//            if (item == null || item.getType() == Material.AIR) continue;
//
//            player.getInventory().setItem(slotOpt.getAsInt(), item);
//        }
    }

    public static void apply(Player player, ClassEnum classEnum) {
        apply(player, classEnum.customClass);
    }

    private static void clearStorageContents(Player player) {
        for (int i = 0; i < 36; i++) {
            player.getInventory().setItem(i, null);
        }
    }

}
