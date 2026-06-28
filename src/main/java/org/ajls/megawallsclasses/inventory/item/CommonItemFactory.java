package org.ajls.megawallsclasses.inventory.item;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.NameSpacedKeys;
import org.ajls.megawallsclasses.inventory.ItemKey;
import org.ajls.trackercompass.CompassItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class CommonItemFactory {

    public static ItemStack create(ItemKey key, Player player) {
        switch (key) {
            case TOOLKIT:
                return mark(key, createToolkit());

            case COMPASS:
                return mark(key, CompassItemStack.trackerCompass.clone());

            case PICKAXE:
                return mark(key, createPickaxe());

            case ENDER_CHEST:
                return mark(key, named(new ItemStack(Material.ENDER_CHEST), "ender_chest"));

            case NETHER_STAR:
                return mark(key, named(new ItemStack(Material.NETHER_STAR), "nether_star"));

            case FOOD:
                return mark(key, named(new ItemStack(Material.COOKED_PORKCHOP, 64), "cooked_porkchop"));

            case ARROW:
                return mark(key, named(new ItemStack(Material.ARROW, 64), "arrow"));

            case BLOCK_1:
                return mark(key, named(new ItemStack(Material.COBBLESTONE, 64), "cobblestone"));

            case BLOCK_2:
                return mark(key, named(new ItemStack(Material.OAK_PLANKS, 64), "plank"));

            case AXE:
                return mark(key, createAxe());

            case HAY_BLOCK:
                return mark(key, named(new ItemStack(Material.HAY_BLOCK, 16), "hay_block"));

            default:
                throw new IllegalArgumentException("No common item for key: " + key);
        }
    }

    private static ItemStack mark(ItemKey key, ItemStack item) {
        ItemStackU.setStringPersistentData(item, NameSpacedKeys.ITEM_TYPE, key.id());
        return item;
    }
}
