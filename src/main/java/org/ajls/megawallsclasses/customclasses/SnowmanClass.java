package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static org.ajls.megawallsclasses.ItemStackModify.setDisplayName;

public class SnowmanClass extends AbstractCustomClass {
    @Override
    public void initializeClassItems() {
        addClassItem(ItemKind.SNOWMAN_SHOVEL, () -> {
            ItemStack diamond_shovel = new ItemStack(Material.DIAMOND_SHOVEL, 1);
            ItemStackU.setDisplayName(diamond_shovel, "diamond_shovel");
            return diamond_shovel;
        });
        addClassItem(ItemKind.SNOWMAN_SNOW_BLOCK, () -> {
            ItemStack snow_block_1 = new ItemStack(Material.SNOW_BLOCK, 64);
            setDisplayName(snow_block_1, "snow_block_1");
            return snow_block_1;
        }, 2);
        addClassItem(ItemKind.SNOWMAN_PUMPKIN, () -> {
            ItemStack pumpkin = new ItemStack(Material.PUMPKIN, 64);
            setDisplayName(pumpkin, "pumpkin");
            return pumpkin;
        });



    }

}
