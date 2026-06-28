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
    //用来将实际将物品放入顺序的

    private final LayoutResolver layoutResolver;
    private final ClassRegistry classRegistry;

    public InventoryApplier(LayoutResolver layoutResolver, ClassRegistry classRegistry) {
        this.layoutResolver = layoutResolver;
        this.classRegistry = classRegistry;
    }

    public void apply(Player player) {
        player.getInventory().clear();

        AbstractCustomClass customClass = ClassEnum.getCustomClassByPlayer(player);

        InventoryLayout layout = layoutResolver.resolve(player, customClass);

        for (LayoutKey key : customClass.getDefaultLayoutKeys()) {
            OptionalInt slotOpt = layout.getSlot(key);
            if (slotOpt.isEmpty()) continue;

            ItemStack item = customClass.createInventoryItem(player, key);
            if (item == null || item.getType() == Material.AIR) continue;

            player.getInventory().setItem(slotOpt.getAsInt(), item);
        }
    }
}
