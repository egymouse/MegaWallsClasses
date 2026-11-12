package org.ajls.megawallsclasses.customclasses;

import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class UndeadKnightClass extends AbstractCustomClass {
    @Override
    public ItemStack createClassSword() {
        ItemStack classSword = ClassItemFactory.classSword(Material.STONE_SWORD);
        classSword.addEnchantment(Enchantment.SHARPNESS, 2);
        return classSword;
    }

    @Override
    public ItemStack createClassBow() {
        ItemStack classBow = ClassItemFactory.classBow();
        classBow.addEnchantment(Enchantment.POWER, 2);
        return classBow;
    }


    //todo 带回来死灵一开始的骷髅马设计 老职业回忆大厅
}
