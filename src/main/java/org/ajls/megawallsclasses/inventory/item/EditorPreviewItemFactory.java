package org.ajls.megawallsclasses.inventory.item;

import org.ajls.megawallsclasses.inventory.key.LayoutKey;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class EditorPreviewItemFactory {

    private EditorPreviewItemFactory() {
    }

    public static ItemStack commonPreview(Player player, LayoutKey key) {
        switch (key.kind()) {
            case SWORD:
                return ClassItemFactory.classSword(Material.IRON_SWORD, key);

            case BOW:
                return ClassItemFactory.classBow(key);

            case HEAL_POTION:
                return ClassItemFactory.healPotion(16, 2, key.index());

            case SPEED_POTION:
                return ClassItemFactory.speedPotion(300, 1, 2, key.index());

            default:
                return CommonItemFactory.create(player, key);
        }
    }
}
