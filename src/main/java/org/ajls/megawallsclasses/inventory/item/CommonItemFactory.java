package org.ajls.megawallsclasses.inventory.item;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.NameSpacedKeys;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.ajls.megawallsclasses.inventory.key.LayoutKey;
import org.ajls.trackercompass.CompassItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import static org.ajls.megawallsclasses.ItemStackModify.*;

public final class CommonItemFactory {

    private CommonItemFactory() {
    }

    public static ItemStack create(Player player, LayoutKey key) {
        ItemStack item;

        switch (key.kind()) {
            case TOOLKIT:
                item = createToolkit();
                break;

            case COMPASS:
                item = CompassItemStack.trackerCompass.clone();
                setDisplayName(item, "compass");
                break;

            case PICKAXE:
                item = createPickaxe();
                break;

            case ENDER_CHEST:
                item = named(new ItemStack(Material.ENDER_CHEST), "ender_chest");
                setClassItem(item);
                break;

            case NETHER_STAR:
                item = named(new ItemStack(Material.NETHER_STAR), "nether_star");
                setClassItem(item);
                break;

            case FOOD:
                item = named(new ItemStack(Material.COOKED_PORKCHOP, 64), "cooked_porkchop");
                break;

            case ARROW:
                item = named(new ItemStack(Material.ARROW, 64), "arrow");
                break;

            case BLOCK:
                item = createBlock(key.index());
                break;

            case AXE:
                item = createAxe();
                break;

            case HAY_BLOCK:
                item = named(new ItemStack(Material.HAY_BLOCK, 16), "hay_block");
                break;

            case SQUID_POTION:
                item = org.ajls.megawallsclasses.InventoryManager.squid_potion_for_everyone();
                break;

            default:
                throw new IllegalArgumentException("No common item for key: " + key);
        }

        return mark(key, item);
    }

    private static ItemStack createToolkit() {
        ItemStack item = new ItemStack(Material.CLOCK);
        setDisplayName(item, "超级战墙工具箱");
        setClassItem(item);
        return item;
    }

    private static ItemStack createPickaxe() {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        setDisplayName(item, "diamond_pickaxe");
        setUnbreakable(item);
        item.addEnchantment(Enchantment.EFFICIENCY, 3);
        setClassItem(item);
        return item;
    }

    private static ItemStack createAxe() {
        ItemStack item = new ItemStack(Material.IRON_AXE);
        setDisplayName(item, "iron_axe");
        setUnbreakable(setAttributeAttackDamage(item, 4));
        setClassItem(item);
        return item;
    }

    private static ItemStack createBlock(int index) {
        if (index == 1) {
            return named(new ItemStack(Material.COBBLESTONE, 64), "cobblestone");
        }

        if (index == 2) {
            return named(new ItemStack(Material.OAK_PLANKS, 64), "plank");
        }

        return named(new ItemStack(Material.COBBLESTONE, 64), "block_" + index);
    }

    private static ItemStack named(ItemStack item, String displayName) {
        setDisplayName(item, displayName);
        return item;
    }

    public static ItemStack mark(LayoutKey key, ItemStack item) {
        ItemStackU.setStringPersistentData(item, NameSpacedKeys.ITEM_TYPE, key.id());
        return item;
    }
}
