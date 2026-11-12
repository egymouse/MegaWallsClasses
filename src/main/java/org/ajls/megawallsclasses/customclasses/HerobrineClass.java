package org.ajls.megawallsclasses.customclasses;

import org.ajls.megawallsclasses.InventoryManager;
import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class HerobrineClass extends AbstractCustomClass {
    @Override
    public ItemStack createClassSword() {
        return ClassItemFactory.classSword(Material.DIAMOND_SWORD);
    }
}
