package org.ajls.megawallsclasses.inventory.apply;

import org.ajls.megawallsclasses.ClassEnum;
import org.ajls.megawallsclasses.customclasses.AbstractCustomClass;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.ajls.megawallsclasses.inventory.layout.InventoryLayout;
import org.ajls.megawallsclasses.inventory.layout.LayoutRepository;
import org.bukkit.entity.Player;

public class InventoryApplier {

    public static void apply(Player player) {
        AbstractCustomClass customClass = ClassEnum.getCustomClassByPlayer(player);
        apply(player, customClass);
    }

    public static void apply(Player player, AbstractCustomClass customClass) {
//        clearStorageContents(player);  //自己手动清理

        InventoryLayout layout = LayoutRepository.resolve(player, customClass);
        layout.asMap().forEach((slot, itemCount) -> {
            ItemKind itemKind = itemCount.getKind();
            int count = itemCount.getCount();
            player.getInventory().setItem(slot, customClass.createInventoryItem(itemKind, count));
        });

    }

    public static void apply(Player player, ClassEnum classEnum) {
        apply(player, classEnum.customClass);
    }

//    private static void clearStorageContents(Player player) {
//        for (int i = 0; i < 36; i++) {
//            player.getInventory().setItem(i, null);
//        }
//    }

}
