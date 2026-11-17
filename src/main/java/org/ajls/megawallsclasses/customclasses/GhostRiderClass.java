package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.advanced.HashMapInteger;
import org.ajls.lib.collections.HashBiMap;
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
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static org.ajls.megawallsclasses.EnergyAccumulate.addEnergy;

public class GhostRiderClass extends AbstractCustomClass{
    public static HashBiMap<UUID, UUID> ghostRiderHorse_player = new HashBiMap<UUID, UUID>() {  //todo 改成key是player吧
    };
    public static BukkitTaskMap<UUID> ghostRider_HorseTaskMap = new BukkitTaskMap<>();  //todo 绑定到玩家，毕竟马会变，玩家可不会
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
    public static int GHOST_RIDER_ACTIVE_SKILL_TIME = 5*20;
    public static int GHOST_RIDER_FUSION_EXTRA_TIME = 5*20;

    public static int GHOST_RIDER_HORSE_COOLDOWN = 40*20;
    @Override
    public boolean tryActiveSkillJudge(Player player, MyListener.ActiveSkillTypeEnum activeSkillType) {
        UUID playerUUID = player.getUniqueId();
        if (activeSkillType != MyListener.ActiveSkillTypeEnum.BOW) return true;
        if (Cooldown.player_passiveSkill1Cooldown.containsKey(playerUUID)) return false;
        if (ghostRiderHorse_player.containsValue(playerUUID)) return false;
        AbstractHorse abstractHorse;
        World world = player.getWorld();
        if (Cooldown.player_activeSkillCooldown.containsKey(playerUUID)) {
            fusion(player);
            abstractHorse = getHorse(player, true);
        }
        else {
            abstractHorse = getHorse(player, false);
        }

        GhostRiderBukkitRunnable ghostRiderBukkitRunnable = new GhostRiderBukkitRunnable(player, abstractHorse);


        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        BukkitTask task = scheduler.runTaskTimer(MegaWallsClasses.getPlugin(), ghostRiderBukkitRunnable, 0L, 1L);

        ghostRider_HorseTaskMap.put(playerUUID, task);
        player.getInventory().setHelmet(ClassItemFactory.ghostRiderLantern());
        return false;
    }



    public static int GHOST_RIDER_VENOM_MAX = 3;

    public static HashMapInteger<UUID> ghostRiderVenom = new HashMapInteger<>(GHOST_RIDER_VENOM_MAX, 0, 0);
    //todo 还要解决合并问题，同一个大招会叠之前的招 不合并到时候查询爆炸 可以把性能压力分散到每一个tick而不是最后消失那一刻
    public static HashSet<UUID> ghostRiderChangeHorse = new HashSet<>();

    @Override
    public boolean activeSkill(Player player) {
        boolean isFusion = ghostRiderHorse_player.containsValue(player.getUniqueId());
        if (isFusion) {
            fusion(player);
            switchHorse(player, true);
        }
        else {
            UUID playerUUID = player.getUniqueId();
            player.sendMessage(ChatColor.DARK_PURPLE + " bruh" + ChatColor.RED + "生命回复I 5s " +  ChatColor.GOLD + "伤害吸收I 5s");
            player.getInventory().setHelmet(ClassItemFactory.ghostRiderLantern());
            addEnergy(player, -100);
            ghostRiderVenom.put(playerUUID, GHOST_RIDER_VENOM_MAX);
            Location location = player.getLocation();
            Cooldown.player_activeSkillCooldown.put(player.getUniqueId(), GHOST_RIDER_ACTIVE_SKILL_TIME);
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
                        location_GhostRiderBlockData.put(blockLocation, ghostRiderBlockData);
                        block.setType(Material.SOUL_SAND, false);
                    }


                }
            }
        }

        return true;



    }


    public static void switchHorse(Player player, boolean isFusion) {
        AbstractHorse abstractHorse = getHorse(player, isFusion);

        if (ghostRiderHorse_player.containsValue(player.getUniqueId())) {
            UUID horseUUID = ghostRiderHorse_player.removeByValue(player.getUniqueId());
            ghostRiderHorse_player.put(abstractHorse.getUniqueId(), player.getUniqueId());

            GhostRiderBukkitRunnable ghostRiderBukkitRunnable = ((GhostRiderBukkitRunnable) ghostRider_HorseTaskMap.get(player.getUniqueId()));
//                ghostRiderChangeHorse.add(player.getUniqueId());
            abstractHorse.setPassenger(player);  //这一步应该调用entityDismout导致老马被删除？必须要加一个免死金牌啊 好吧不会因为ghostRiderHorse_player查不到了

            AbstractHorse oldHorse = ghostRiderBukkitRunnable.getHorse();
            oldHorse.remove();
            ghostRiderBukkitRunnable.setHorse(abstractHorse);
        }
    }


    public static AbstractHorse getHorse(Player player, boolean isFusion) {
        World world = player.getWorld();
        AbstractHorse abstractHorse;
        String horseTypeName;
        if (isFusion) {
            abstractHorse = (AbstractHorse) world.spawnEntity(player.getLocation(), EntityType.SKELETON_HORSE);
            horseTypeName = "Skeleton";
        }
        else {
            abstractHorse = (AbstractHorse) world.spawnEntity(player.getLocation(), EntityType.ZOMBIE_HORSE);
            horseTypeName = "Zombie";
        }

        AbstractHorse skeletonHorse = abstractHorse;  //懒得改名了

        ghostRiderHorse_player.put(skeletonHorse.getUniqueId(), player.getUniqueId());
//        skeletonHorse.setRearing(false);
        skeletonHorse.setOwner(player);
        skeletonHorse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        skeletonHorse.setJumpStrength(0.7);
        skeletonHorse.setVelocity(player.getVelocity().multiply(2));
        skeletonHorse.setPassenger(player); //addPassenger do the same thing
        skeletonHorse.setMaxHealth(30);
        skeletonHorse.setHealth(skeletonHorse.getMaxHealth());
        skeletonHorse.setCustomName(ScoreboardU.getPlayerTeamColor(player, true) + player.getName() + "'s " + horseTypeName + " Horse");
        return abstractHorse;
    }



    public static void fusion(Player player) {
        player.sendMessage(ChatColor.DARK_PURPLE + " bruhbruh" + ChatColor.RED + "生命回复I 5s " +  ChatColor.GOLD + "伤害吸收I 5s");
        UUID playerUUID = player.getUniqueId();

        Cooldown.player_activeSkillCooldown.put(playerUUID, Cooldown.player_passiveSkill1Cooldown.get(playerUUID) + GHOST_RIDER_FUSION_EXTRA_TIME);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 12*5, 2));
        player.getWorld().strikeLightningEffect(player.getLocation());
    }


    public static class GhostRiderBukkitRunnable extends BukkitRunnable {
        public AbstractHorse getHorse() {
            return horse;
        }

        public void setHorse(AbstractHorse horse) {
            this.horse = horse;
        }

        public AbstractHorse horse;
        public Player player;// todo 处理玩家下线

        public GhostRiderBukkitRunnable(Player player, AbstractHorse horse) {
            this.horse = horse;
            this.player = player;
        }
        @Override
        public void run() {
            World world = horse.getWorld();
            UUID playerUUID = player.getUniqueId();  //todo 可以静态注入到全局变量
            //                    skeletonHorse.setRearing(false);

            horse.setRearing(false);  //todo 看看效果如何
            Location horseBlockLocation = horse.getLocation().toBlockLocation();
            for (int x = horseBlockLocation.getBlockX() - 2; x <= horseBlockLocation.getBlockX() + 2; x++) {
                for (int y = horseBlockLocation.getBlockY() - 2; y <= horseBlockLocation.getBlockY() + 2; y++) {
                    for (int z = horseBlockLocation.getBlockZ() - 2; z <= horseBlockLocation.getBlockZ() + 2; z++) {
                        Location blockLocation = new Location(world, x, y, z);
//                                blockLocation.getBlock().setType(Material.NETHERRACK);
                        Block block = blockLocation.getBlock();
                        if (block.isPassable()) continue;  //todo 调成更精细的判断 包括洪水填充，snowman，末影人tp穿墙，龙下界岩石

                        //  //todo 这里fusion可以根据set的马是什么类型来判断
                        boolean isFusion = ghostRiderHorse_player.containsValue(player.getUniqueId()) && Cooldown.player_activeSkillCooldown.containsKey(player.getUniqueId());
                        Material blockMaterial = Material.NETHERRACK;
                        GhostRiderSkillType ghostRiderSkillType = GhostRiderSkillType.NETHERRACK;
                        if (isFusion) {
                            blockMaterial = Material.OBSIDIAN;
                            ghostRiderSkillType = GhostRiderSkillType.OBSIDIAN;
                        }
                        if (location_GhostRiderBlockData.containsKey(blockLocation)) {
                            GhostRiderBlockData ghostRiderBlockData = location_GhostRiderBlockData.get(blockLocation);
                            ghostRiderBlockData.put(playerUUID, GHOST_RIDER_NETHERRACK_DISAPPEAR_TIME, block.getState(), ghostRiderSkillType);
                            location_GhostRiderBlockData.putLast(blockLocation, ghostRiderBlockData);
                        } else {
                            GhostRiderBlockData ghostRiderBlockData = new GhostRiderBlockData();
                            ghostRiderBlockData.put(playerUUID, GHOST_RIDER_NETHERRACK_DISAPPEAR_TIME, block.getState(), ghostRiderSkillType);
                            location_GhostRiderBlockData.put(blockLocation, ghostRiderBlockData);

                        }
                        block.setType(blockMaterial, false);
                    }
                }
            }
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
