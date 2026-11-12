package org.ajls.megawallsclasses.customclasses;

import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class SkeletonLordClass extends AbstractCustomClass{
    @Override
    public ItemStack createClassSword() {
        ItemStack classSword = ClassItemFactory.classSword(Material.STICK, false);  //you can't add unbreaking on a stick
        classSword.addUnsafeEnchantment(Enchantment.SHARPNESS, 4);
        return classSword;
    }
}
