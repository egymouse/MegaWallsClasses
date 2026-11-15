package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.advanced.HashMapInteger;
import org.ajls.lib.advanced.HaxhMap;
import org.ajls.lib.utils.ScoreboardU;
import org.ajls.megawallsclasses.BukkitTaskMap;
import org.ajls.megawallsclasses.Cooldown;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.MyListener;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

import static org.ajls.megawallsclasses.EnergyAccumulate.addEnergy;

public class GhostRiderClass extends AbstractCustomClass{
    public static HaxhMap<UUID, UUID> ghostRider_horse = new HaxhMap<>();
    public static BukkitTaskMap<UUID> ghostRiderHorseTaskMap = new BukkitTaskMap<>();
//    HashMap<Integer, > timeStamp_blockData


    public static class GhostRiderBlockData{
        private BlockState blockState;
        private UUID uuid;

    }

    @Override
    public boolean tryActiveSkillJudge(Player player, MyListener.ActiveSkillTypeEnum activeSkillType) {
        UUID playerUUID = player.getUniqueId();
        if (activeSkillType == MyListener.ActiveSkillTypeEnum.BOW) {
            if (!Cooldown.player_passiveSkill1Cooldown.containsKey(playerUUID)) {
                World world = player.getWorld();
                SkeletonHorse skeletonHorse = (SkeletonHorse) world.spawnEntity(player.getLocation(), EntityType.SKELETON_HORSE);
                ghostRider_horse.put(player.getUniqueId(), skeletonHorse.getUniqueId());
//        skeletonHorse.setRearing(false);
                skeletonHorse.setOwner(player);
                skeletonHorse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                skeletonHorse.setJumpStrength(1.0);
                skeletonHorse.setVelocity(player.getVelocity().multiply(2));
                skeletonHorse.setPassenger(player); //addPassenger do the same thing
                skeletonHorse.setMaxHealth(30);
                skeletonHorse.setCustomName(ScoreboardU.getPlayerTeamColor(player, true) + player.getName() + "'s Skeleton Horse");
                ghostRider_horse.put(player.getUniqueId(), skeletonHorse.getUniqueId());

                BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                BukkitTask task = scheduler.runTaskTimer(MegaWallsClasses.getPlugin(), () -> {
//                    skeletonHorse.setRearing(false);

                    Location horseBlockLocation = skeletonHorse.getLocation().toBlockLocation();
                    for (int x = horseBlockLocation.getBlockX() - 1; x <= horseBlockLocation.getBlockX() + 1; x++) {
                        for (int y  = horseBlockLocation.getBlockY(); y <= horseBlockLocation.getBlockY() + 1; y++) {
                            for (int z = horseBlockLocation.getBlockZ() - 1; z <= horseBlockLocation.getBlockZ() + 1; z++) {
                                Location blockLocation = new Location(world, x, y, z);
                                blockLocation.getBlock().setType(Material.NETHERRACK);
                            }
                        }
                    }

                    // ./
                },0L , 1L);
                Cooldown.player_passiveSkill1Cooldown.put(playerUUID, 40*20);


            }
            return false;
        }
        return true;
    }

    public static int GHOST_RIDER_VENOM_MAX = 3;

    public static HashMapInteger<UUID> ghostRiderVenom = new HashMapInteger<>(GHOST_RIDER_VENOM_MAX, 0, 0);

    @Override
    public void activeSkill(Player player) {
        UUID playerUUID = player.getUniqueId();
        player.sendMessage(ChatColor.DARK_PURPLE + " " + ChatColor.RED + "生命回复I 5s " +  ChatColor.GOLD + "伤害吸收I 5s");
        addEnergy(player, -100);
        ghostRiderVenom.put(playerUUID, GHOST_RIDER_VENOM_MAX);
        Cooldown.player_activeSkillCooldown.put(player.getUniqueId(), 5*20);
    }
}
