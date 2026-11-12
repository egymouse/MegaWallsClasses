package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.NameSpacedKeys;
import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import static org.ajls.megawallsclasses.ItemStackModify.setDisplayName;

public class SnowmanClass extends AbstractCustomClass {
    @Override
    public ItemStack createClassSword() {
        ItemStack classSword = ClassItemFactory.classSword(Material.DIAMOND_SWORD);
        return classSword;
    }

    @Override
    public void initializeClassItems() {
        addClassItem(ItemKind.SNOWMAN_SHOVEL, () -> {
            ItemStack diamond_shovel = new ItemStack(Material.DIAMOND_SHOVEL, 1);
            ItemStackU.setStringPersistentData(diamond_shovel, NameSpacedKeys.ITEM_CATEGORY, AbstractCustomClass.CLASS_DIFF);
            return diamond_shovel;
        });
        addClassItem(ItemKind.SNOWMAN_SNOW_BLOCK, () -> {
            ItemStack snow_block_1 = new ItemStack(Material.SNOW_BLOCK, 64);
            ItemStackU.setStringPersistentData(snow_block_1, NameSpacedKeys.ITEM_CATEGORY, AbstractCustomClass.CLASS_DIFF);
            return snow_block_1;
        }, 2);
        addClassItem(ItemKind.SNOWMAN_PUMPKIN, () -> {
            ItemStack pumpkin = new ItemStack(Material.PUMPKIN, 64);
            ItemStackU.setStringPersistentData(pumpkin, NameSpacedKeys.ITEM_CATEGORY, AbstractCustomClass.CLASS_DIFF);
            return pumpkin;
        });



    }

}
