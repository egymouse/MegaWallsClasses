package org.ajls.megawallsclasses.customclasses;

import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ShamanClass extends AbstractCustomClass {
    @Override
    public ItemStack createClassSword() {
        ItemStack classSword = ClassItemFactory.classSword(Material.DIAMOND_SWORD);
        return classSword;
    }

}
