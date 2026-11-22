package org.ajls.megawallsclasses.customclasses;

import org.ajls.megawallsclasses.InitializeClass;
import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class WardenClass extends AbstractCustomClass {
    public static int WARDEN_MAX_SENSOR_COUNT = 2;
    public static int WARDEN_SENSOR_COOLDOWN_TICK = 15*20;
    public static int WARDEN_ACTIVE_SKILL_COOLDOWN = 5*20;

    //                classSword = getClassSword(Material.DIAMOND_SWORD);
    //                classReorderInventory.addItem(warden_initial_sensor());
    //                classBow = getClassBow();
    //                classBow.addEnchantment(Enchantment.POWER, 1);
    //                break;
    //一看就太强了，坦克还有神秘d剑和力量1

    @Override
    public ItemStack createClassSword() {
        ItemStack classSword = ClassItemFactory.classSword(Material.DIAMOND_SWORD);
        return classSword;
    }

    @Override
    public void initializeClassItems() {
        addClassItem(ItemKind.WARDEN_SENSOR, () -> {
            return ClassItemFactory.wardenSensor();
        });
    }
}
