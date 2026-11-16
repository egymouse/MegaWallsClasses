package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.advanced.HashMapInteger;
import org.ajls.lib.collections.HashBiMap;
import org.ajls.lib.utils.ScoreboardU;
import org.ajls.megawallsclasses.BukkitTaskMap;
import org.ajls.megawallsclasses.Cooldown;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.MyListener;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static org.ajls.megawallsclasses.EnergyAccumulate.addEnergy;

public class GhostRiderClass extends AbstractCustomClass{
    public static HashBiMap<UUID, UUID> ghostRiderHorse_player = new HashBiMap<UUID, UUID>() {
    };
    public static BukkitTaskMap<UUID> ghostRiderHorseTaskMap = new BukkitTaskMap<>();
//    HashMap<Integer, > timeStamp_blockData
    public static LinkedHashMap<Location, GhostRiderBlockData> location_GhostRiderBlockData = new LinkedHashMap<>();


    public static void tick() {
        Iterator< Map.Entry<Location, GhostRiderBlockData>> iterator = location_GhostRiderBlockData.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Location, GhostRiderBlockData> ghostRiderBlockDataEntry = iterator.next();
//            GhostRiderBlockData ghostRiderBlockData = entry.getValue();
        }


    }


    public static int GHOST_RIDER_NETHERRACK_DISAPPEAR_TIME = 4*20;
    @Override
    public boolean tryActiveSkillJudge(Player player, MyListener.ActiveSkillTypeEnum activeSkillType) {
        UUID playerUUID = player.getUniqueId();
        if (activeSkillType == MyListener.ActiveSkillTypeEnum.BOW) {
            if (!Cooldown.player_passiveSkill1Cooldown.containsKey(playerUUID)) {
                if (!player.isInsideVehicle()) {
                    World world = player.getWorld();
                    SkeletonHorse skeletonHorse = (SkeletonHorse) world.spawnEntity(player.getLocation(), EntityType.SKELETON_HORSE);
                    ghostRiderHorse_player.put(skeletonHorse.getUniqueId(), player.getUniqueId());
//        skeletonHorse.setRearing(false);
                    skeletonHorse.setOwner(player);
                    skeletonHorse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                    skeletonHorse.setJumpStrength(1.0);
                    skeletonHorse.setVelocity(player.getVelocity().multiply(2));
                    skeletonHorse.setPassenger(player); //addPassenger do the same thing
                    skeletonHorse.setMaxHealth(30);
                    skeletonHorse.setCustomName(ScoreboardU.getPlayerTeamColor(player, true) + player.getName() + "'s Skeleton Horse");


                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                    BukkitTask task = scheduler.runTaskTimer(MegaWallsClasses.getPlugin(), () -> {
//                    skeletonHorse.setRearing(false);

                        Location horseBlockLocation = skeletonHorse.getLocation().toBlockLocation();
                        for (int x = horseBlockLocation.getBlockX() - 1; x <= horseBlockLocation.getBlockX() + 1; x++) {
                            for (int y  = horseBlockLocation.getBlockY(); y <= horseBlockLocation.getBlockY() + 1; y++) {
                                for (int z = horseBlockLocation.getBlockZ() - 1; z <= horseBlockLocation.getBlockZ() + 1; z++) {
                                    Location blockLocation = new Location(world, x, y, z);
//                                blockLocation.getBlock().setType(Material.NETHERRACK);
                                    Block block = blockLocation.getBlock();
                                    if (location_GhostRiderBlockData.containsKey(blockLocation)) {
                                        GhostRiderBlockData ghostRiderBlockData = location_GhostRiderBlockData.get(blockLocation);
                                        ghostRiderBlockData.put(playerUUID, GHOST_RIDER_NETHERRACK_DISAPPEAR_TIME);
                                        location_GhostRiderBlockData.putLast(blockLocation, ghostRiderBlockData);
                                    }
                                    else {
                                        GhostRiderBlockData ghostRiderBlockData = new GhostRiderBlockData(block.getState());
                                        ghostRiderBlockData.put(playerUUID, GHOST_RIDER_NETHERRACK_DISAPPEAR_TIME);
                                        location_GhostRiderBlockData.put(blockLocation, ghostRiderBlockData);

                                    }
                                }
                            }
                        }

                        // ./
                    },0L , 1L);
                    Cooldown.player_passiveSkill1Cooldown.put(playerUUID, 40*20);
                }
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

    public static class GhostRiderBlockData {
        HashMap<UUID, Integer> playerUUID_timeStamp = new HashMap<>();
        private BlockState blockState;


        public GhostRiderBlockData(BlockState blockState) {
            setBlockState(blockState);
        }

        // Getter / Setter（可按需添加校验）
        public BlockState getBlockState() {
            return blockState;
        }

        public void setBlockState(BlockState blockState) {
            if (blockState == null) throw new IllegalArgumentException("blockState must not be null");
            this.blockState = blockState;
        }



        public void put(UUID uuid, int offset) {
            playerUUID_timeStamp.put(uuid, Bukkit.getCurrentTick() + offset);
        }

        public boolean valid() {
            int currentTick = Bukkit.getCurrentTick();
            Iterator<Map.Entry<UUID, Integer>> it = playerUUID_timeStamp.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<UUID, Integer> entry = it.next();
                int timeStamp = entry.getValue();
                UUID uuid = entry.getKey();
                if (entry.getValue() <= currentTick) {
                    it.remove();   // 通过 iterator 移除，安全
                }
                else {
                    return true;
                }
            }
            return false;

        }



    }
}
