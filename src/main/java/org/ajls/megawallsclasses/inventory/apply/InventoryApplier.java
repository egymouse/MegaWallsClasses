package org.ajls.megawallsclasses.inventory.apply;

import org.ajls.megawallsclasses.inventory.ClassKit;
import org.ajls.megawallsclasses.inventory.ClassRegistry;
import org.ajls.megawallsclasses.inventory.ItemKey;
import org.ajls.megawallsclasses.inventory.layout.InventoryLayout;
import org.ajls.megawallsclasses.inventory.layout.LayoutResolver;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.OptionalInt;

public class InventoryApplier {

    private final LayoutResolver layoutResolver;
    private final ClassRegistry classRegistry;

    public InventoryApplier(LayoutResolver layoutResolver, ClassRegistry classRegistry) {
        this.layoutResolver = layoutResolver;
        this.classRegistry = classRegistry;
    }

    public void apply(Player player, int classId) {
        player.getInventory().clear();

        ClassKit kit = classRegistry.get(classId);
        InventoryLayout layout = layoutResolver.resolve(player, kit);

        for (ItemKey key : kit.itemKeys()) {
            OptionalInt slotOpt = layout.getSlot(key);
            if (slotOpt.isEmpty()) continue;

            ItemStack item = kit.createItem(key, player);
            if (item == null || item.getType() == Material.AIR) continue;

            player.getInventory().setItem(slotOpt.getAsInt(), item);
        }
    }
}
