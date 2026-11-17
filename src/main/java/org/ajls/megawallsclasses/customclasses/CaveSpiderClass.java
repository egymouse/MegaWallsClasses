package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.advanced.BukkitTaskMap;
import org.ajls.lib.advanced.HashMapInteger;
import org.ajls.megawallsclasses.ClassEnum;
import org.ajls.megawallsclasses.Cooldown;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.UUID;

import static java.lang.Math.abs;
import static org.ajls.megawallsclasses.EnergyAccumulate.addEnergy;

public class CaveSpiderClass extends AbstractCustomClass{
    @Override
    public ItemStack createClassSword() {
        ItemStack classSword = ClassItemFactory.classSword(Material.DIAMOND_SWORD);
        classSword.addEnchantment(Enchantment.BANE_OF_ARTHROPODS, 4);
        return classSword;
    }

    @Override
    public void initializeClassItems() {
        addClassItem(ItemKind.CAVE_SPIDER_SHOVEL, ClassItemFactory::caveSpiderShovel);
    }

    public static final int CAVE_SPIDER_VENOM_MAX = 4;

    public static BukkitTaskMap<UUID> caveSpiderTaskMap = new BukkitTaskMap<>();

    @Override
    public boolean activeSkill(Player player) {
        UUID playerUUID = player.getUniqueId();
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0, true, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100 , 0, true, true));
        float pitch = -abs(player.getLocation().getPitch());
        Location location = player.getLocation();
        location.setPitch(pitch);
        Vector vector = location.getDirection();
        String jumpType = "大跳";
        double velMultiplier = 1.3;
        if (caveSpiderMode.get(playerUUID) == 1) {
            jumpType = "小跳";
            velMultiplier = 1.2;
        }
        player.setVelocity(vector.multiply(velMultiplier));

        player.sendMessage(ChatColor.DARK_PURPLE + jumpType + " " + ChatColor.RED + "生命回复I 5s " +  ChatColor.GOLD + "伤害吸收I 5s");
        addEnergy(player, -100);
        caveSpiderVenom.put(playerUUID, CAVE_SPIDER_VENOM_MAX);
//        caveSpiderFlyby.put(playerUUID, CAVE_SPIDER_VENOM_MAX);

//        caveSpiderVenomTimestamp.put(playerUUID, 0);
//        Cooldown.player_passiveSkill2Cooldown.put(playerUUID, 60);

        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(MegaWallsClasses.getPlugin(), () -> {
            if (player.isOnGround()) {
                caveSpiderTaskMap.remove(player.getUniqueId());
                World world = player.getWorld();
                Location currentLocation = player.getLocation();
                world.createExplosion(currentLocation, 3f, false, true, player);
            }
        },5,1);  //2
        caveSpiderTaskMap.put(player.getUniqueId(), bukkitTask);
        return true;

    }

//    public static HashMapInteger<UUID> caveSpiderVenomTimestamp = new HashMapInteger<>();
    public static HashMapInteger<UUID> caveSpiderVenom = new HashMapInteger<>(CAVE_SPIDER_VENOM_MAX, 0);
    public static HashMapInteger<UUID> caveSpiderFlyby = new HashMapInteger<>(CAVE_SPIDER_VENOM_MAX, 0, CAVE_SPIDER_VENOM_MAX);


    public static HashMapInteger<UUID> caveSpiderMode = new HashMapInteger<>(1, 0, 0);  //mode 0是长
}
