package org.ajls.megawallsclasses.customclasses;

import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class SkeletonClass extends AbstractCustomClass {
    @Override
    public ItemStack createClassBow() {
        ItemStack classBow = ClassItemFactory.classBow();
        classBow.addEnchantment(Enchantment.POWER, 3);
        return classBow;
    }

}
