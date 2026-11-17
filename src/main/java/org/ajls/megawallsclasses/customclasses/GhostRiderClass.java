package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.advanced.HashMapInteger;
import org.ajls.lib.collections.HashBiMap;
import org.ajls.lib.utils.GamemodeU;
import org.ajls.lib.utils.PlayerU;
import org.ajls.lib.utils.ScoreboardU;
import org.ajls.megawallsclasses.BukkitTaskMap;
import org.ajls.megawallsclasses.Cooldown;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.MyListener;
import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
    public static int GHOST_RIDER_SOUL_SAND_DISAPPEAR_TIME = 4*20;
    public static int GHOST_RIDER_OBSIDIAN_DISAPPEAR_TIME = 4*20;
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
                            for (int y  = horseBlockLocation.getBlockY() - 1; y <= horseBlockLocation.getBlockY() + 1; y++) {
                                for (int z = horseBlockLocation.getBlockZ() - 1; z <= horseBlockLocation.getBlockZ() + 1; z++) {
                                    Location blockLocation = new Location(world, x, y, z);
//                                blockLocation.getBlock().setType(Material.NETHERRACK);
                                    Block block = blockLocation.getBlock();
                                    if (block.isPassable()) continue;  //todo 调成更精细的判断 包括洪水填充，snowman，末影人tp穿墙，龙下界岩石
                                    if (location_GhostRiderBlockData.containsKey(blockLocation)) {
                                        GhostRiderBlockData ghostRiderBlockData = location_GhostRiderBlockData.get(blockLocation);
                                        ghostRiderBlockData.put(playerUUID, GHOST_RIDER_NETHERRACK_DISAPPEAR_TIME, block.getState(), GhostRiderSkillType.NETHERRACK);
                                        location_GhostRiderBlockData.putLast(blockLocation, ghostRiderBlockData);
                                    }
                                    else {
                                        GhostRiderBlockData ghostRiderBlockData = new GhostRiderBlockData();
                                        ghostRiderBlockData.put(playerUUID, GHOST_RIDER_NETHERRACK_DISAPPEAR_TIME, block.getState() , GhostRiderSkillType.NETHERRACK);
                                        location_GhostRiderBlockData.put(blockLocation, ghostRiderBlockData);

                                    }
                                    block.setType(Material.NETHERRACK, false);
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
    //todo 还要解决合并问题，同一个大招会叠之前的招 不合并到时候查询爆炸 可以把性能压力分散到每一个tick而不是最后消失那一刻

    @Override
    public void activeSkill(Player player) {
        player.getInventory().setHelmet(ClassItemFactory.ghostRiderPumpkin());
        if (!player.isInsideVehicle()) {  //todo 可以绕过
            UUID playerUUID = player.getUniqueId();
            player.sendMessage(ChatColor.DARK_PURPLE + " bruh" + ChatColor.RED + "生命回复I 5s " +  ChatColor.GOLD + "伤害吸收I 5s");
            addEnergy(player, -100);
            ghostRiderVenom.put(playerUUID, GHOST_RIDER_VENOM_MAX);
            Location location = player.getLocation();
            Cooldown.player_activeSkillCooldown.put(player.getUniqueId(), 5*20);
            for (int x = location.getBlockX() - 2; x <= location.getBlockX() + 2; x++) {
                for (int y = location.getBlockY() - 2; y <= location.getBlockY() + 2; y++) {
                    for (int z = location.getBlockZ() - 2; z <= location.getBlockZ() + 2; z++) {
                        Location blockLocation = new Location(location.getWorld(), x, y, z);
                        Block block = blockLocation.getBlock();
                        if (block.isPassable()) continue;
                        GhostRiderBlockData ghostRiderBlockData = new GhostRiderBlockData();
                        if (location_GhostRiderBlockData.containsKey(blockLocation)) {
                            ghostRiderBlockData = location_GhostRiderBlockData.get(blockLocation);  //todo 如果存在直接改就行了不用put回去
                        }
                        ghostRiderBlockData.put(playerUUID, GHOST_RIDER_SOUL_SAND_DISAPPEAR_TIME, block.getState(), GhostRiderSkillType.SOUL_SAND);
                        block.setType(Material.SOUL_SAND, false);
                    }


                }
            }
        }
        else {

        }

    }

    public static class GhostRiderBlockData {
        //todo 维护一个stack 增加每个玩家入度/出度来方便valid区分有没有玩家
        ArrayList<GhostRiderPlayerSkill> ghostRiderPlayerSkills = new ArrayList<>();



        public GhostRiderBlockData() {
        }




        public void put(UUID uuid, int offset, BlockState originalBlockState, GhostRiderSkillType ghostRiderSkillType) {
            ghostRiderPlayerSkills.add(new GhostRiderPlayerSkill(uuid, Bukkit.getCurrentTick() + offset, originalBlockState, ghostRiderSkillType));
        }


        public boolean tick() {
            int currentTick = Bukkit.getCurrentTick();


            BlockState originalBlockState = null;
            for (int i = ghostRiderPlayerSkills.size() - 1; i >= 0; i--) {
                GhostRiderPlayerSkill ghostRiderPlayerSkill = ghostRiderPlayerSkills.get(i);
                int timeStamp = ghostRiderPlayerSkill.getTimeStamp();
                if (timeStamp <= currentTick) {
                    ghostRiderPlayerSkills.remove(i);
                    originalBlockState = ghostRiderPlayerSkill.getOriginalBlockState();
                }
                else {
                    break;
                }
            }
            if (originalBlockState != null) {
                originalBlockState.update(true, false);
            }
            if (ghostRiderPlayerSkills.isEmpty()) {
                return true;
            }
            return false;
        }

        public void apply(Player player) {
            for (int i = ghostRiderPlayerSkills.size() - 1; i >= 0; i--) {
                GhostRiderPlayerSkill ghostRiderPlayerSkill = ghostRiderPlayerSkills.get(i);
                UUID uuid = ghostRiderPlayerSkill.getPlayerUUID();
                Player possibleEnemy = Bukkit.getPlayer(uuid);
                if (possibleEnemy == null ) continue;
                if (PlayerU.isPlayerSameTeam(player, possibleEnemy)) continue;
                GhostRiderSkillType ghostRiderSkillType =  ghostRiderPlayerSkill.getGhostRiderSkillType();
                if (ghostRiderSkillType == null) continue;
                else if (ghostRiderSkillType == GhostRiderSkillType.NETHERRACK) {

                }
                else if (ghostRiderSkillType == GhostRiderSkillType.SOUL_SAND) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 0 , true, true));
                }
                else if (ghostRiderSkillType == GhostRiderSkillType.OBSIDIAN) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 0 , true, true));
                    if (player.getFireTicks() < 60) {
                        player.setFireTicks(60);  //todo 龙的弓和大招也一样
                    }
                }
            }


        }



    }

    public enum GhostRiderSkillType {
        NETHERRACK,
        SOUL_SAND,
        OBSIDIAN,

    }

    public static class GhostRiderPlayerSkill {
        UUID playerUUID;
        int timeStamp;
        BlockState originalBlockState;
        GhostRiderSkillType ghostRiderSkillType;

        public GhostRiderSkillType getGhostRiderSkillType() {
            return ghostRiderSkillType;
        }

        public void setGhostRiderSkillType(GhostRiderSkillType ghostRiderSkillType) {
            this.ghostRiderSkillType = ghostRiderSkillType;
        }

        GhostRiderPlayerSkill(UUID playerUUID, int timeStamp, BlockState originalBlockState, GhostRiderSkillType  ghostRiderSkillType) {
            this.playerUUID = playerUUID;
            this.timeStamp = timeStamp;
            this.originalBlockState = originalBlockState;
            this.ghostRiderSkillType = ghostRiderSkillType;
        }

        public UUID getPlayerUUID() {
            return playerUUID;
        }

        public void setPlayerUUID(UUID playerUUID) {
            this.playerUUID = playerUUID;
        }

        public int getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(int timeStamp) {
            this.timeStamp = timeStamp;
        }

        public BlockState getOriginalBlockState() {
            return originalBlockState;
        }

        public void setOriginalBlockState(BlockState originalBlockState) {
            this.originalBlockState = originalBlockState;
        }
    }
}
