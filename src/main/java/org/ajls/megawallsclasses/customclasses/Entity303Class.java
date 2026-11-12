package org.ajls.megawallsclasses.customclasses;

import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Entity303Class extends AbstractCustomClass {

    @Override
    public ItemStack createClassBow() {
        ItemStack classBow = ClassItemFactory.classBow();
        classBow.addEnchantment(Enchantment.FLAME, 1);
        classBow.addEnchantment(Enchantment.POWER, 1);
        return classBow;
    }

}
