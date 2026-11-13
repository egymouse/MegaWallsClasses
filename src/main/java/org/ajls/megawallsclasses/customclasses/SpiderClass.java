package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.advanced.BukkitTaskMap;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.UUID;

import static java.lang.Math.abs;
import static org.ajls.megawallsclasses.EnergyAccumulate.addEnergy;

public class SpiderClass extends AbstractCustomClass {

    @Override
    public ItemStack createClassSword() {
        ItemStack classSword = ClassItemFactory.classSword(Material.DIAMOND_SWORD);
        return classSword;
    }


}
