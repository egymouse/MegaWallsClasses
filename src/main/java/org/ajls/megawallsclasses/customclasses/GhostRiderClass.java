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
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static org.ajls.megawallsclasses.EnergyAccumulate.addEnergy;

public class GhostRiderClass extends AbstractCustomClass{
    public static HashBiMap<UUID, UUID> ghostRiderHorse_player = new HashBiMap<UUID, UUID>() {  //todo 改成key是player吧
    };
    public static BukkitTaskMap<UUID> ghostRider_HorseTaskMap = new BukkitTaskMap<>();  //todo 绑定到玩家，毕竟马会变，玩家可不会
//    HashMap<Integer, > timeStamp_blockData
    public static HashMap<Location, GhostRiderBlockData> location_GhostRiderBlockData = new LinkedHashMap<>();

    public static BukkitTaskMap<UUID> ghostRider_activeSkillTaskMap = new BukkitTaskMap<>();


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
            ghostRider_activeSkillTaskMap.remove(playerUUID);
            fusion(player);
            abstractHorse = getHorse(player, true);
        }
        else {
            abstractHorse = getHorse(player, false);
        }

        GhostRiderRunnable ghostRiderRunnable = new GhostRiderRunnable(player, abstractHorse);


        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        BukkitTask task = scheduler.runTaskTimer(MegaWallsClasses.getPlugin(), ghostRiderRunnable, 0L, 1L);

        ghostRider_HorseTaskMap.put(playerUUID, task);
        player.getInventory().setHelmet(ClassItemFactory.ghostRiderLantern());
        return false;
    }



    public static int GHOST_RIDER_VENOM_MAX = 3;

    public static HashMapInteger<UUID> ghostRiderVenom = new HashMapInteger<>(GHOST_RIDER_VENOM_MAX, 0, 0);
    //todo 还要解决合并问题，同一个大招会叠之前的招 不合并到时候查询爆炸 可以把性能压力分散到每一个tick而不是最后消失那一刻
    //todo 还有同一个玩家高级别的要覆盖低级别的 比如黑曜石>灵魂沙>地狱岩，当然不同玩家就自由覆盖

    @Override
    public boolean activeSkill(Player player) {
        Cooldown.player_activeSkillCooldown.put(player.getUniqueId(), GHOST_RIDER_ACTIVE_SKILL_TIME);

        UUID playerUUID = player.getUniqueId();
        boolean isFusion = ghostRiderHorse_player.containsValue(player.getUniqueId());
        addEnergy(player, -100);
        if (isFusion) {
            ghostRider_activeSkillTaskMap.remove(player.getUniqueId());
            fusion(player);
            switchHorse(player, true);
        }
        else {
            player.sendMessage(ChatColor.DARK_PURPLE + " bruh" + ChatColor.RED + "生命回复I 5s " +  ChatColor.GOLD + "伤害吸收I 5s");
            player.getInventory().setHelmet(ClassItemFactory.ghostRiderLantern());

            ghostRiderVenom.put(playerUUID, GHOST_RIDER_VENOM_MAX);
            activeNormalSkillTask(player);






        }

        return true;



    }

    public static void activeNormalSkillTask(Player player) {
        if (player == null) return;
        UUID playerUUID = player.getUniqueId();
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        BukkitTask task = scheduler.runTaskTimer(MegaWallsClasses.getPlugin(), () -> {
            Location location = player.getLocation();
            for (int x = location.getBlockX() - 2; x <= location.getBlockX() + 2; x++) {
                for (int y = location.getBlockY() - 2; y <= location.getBlockY() + 2; y++) {
                    for (int z = location.getBlockZ() - 2; z <= location.getBlockZ() + 2; z++) {
                        Location blockLocation = new Location(location.getWorld(), x, y, z);
                        Block block = blockLocation.getBlock();
//                        Location realBlockLocation = block.getLocation();
//                        blockLocation = realBlockLocation;
                        if (block.isPassable()) continue;
                        GhostRiderBlockData ghostRiderBlockData = new GhostRiderBlockData();
                        if (location_GhostRiderBlockData.containsKey(blockLocation)) {
                            ghostRiderBlockData = location_GhostRiderBlockData.get(blockLocation);  //todo 如果存在直接改就行了不用put回去
                        }
                        boolean success = ghostRiderBlockData.put(playerUUID, GHOST_RIDER_SOUL_SAND_DISAPPEAR_TIME, block.getState(), GhostRiderSkillType.SOUL_SAND);
                        location_GhostRiderBlockData.put(blockLocation, ghostRiderBlockData);
                        if (success) {
                            block.setType(Material.SOUL_SAND, false);
                        }
                    }


                }
            }
        }, 0, 1);
        ghostRider_activeSkillTaskMap.put(playerUUID, task);
    }


    public static void switchHorse(Player player, boolean isFusion) {
        //一般来说调用这个method的都是有马的


        if (ghostRiderHorse_player.containsValue(player.getUniqueId())) {
            ghostRiderHorse_player.removeByValue(player.getUniqueId());  //先移除再注册
            AbstractHorse abstractHorse = getHorse(player, isFusion);  //todo 这时候玩家在马上，应该用马的位置

            ghostRiderHorse_player.put(abstractHorse.getUniqueId(), player.getUniqueId());

            GhostRiderRunnable ghostRiderBukkitRunnable = (GhostRiderRunnable) (ghostRider_HorseTaskMap.get(player.getUniqueId()).getRunnable());  //.getRunnable()

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

        Cooldown.player_activeSkillCooldown.put(playerUUID, Cooldown.player_activeSkillCooldown.getOrDefault((playerUUID), 0) + GHOST_RIDER_FUSION_EXTRA_TIME);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 12*5, 2));
//        player.getActivePotionEffects()
        player.getWorld().strikeLightningEffect(player.getLocation());
    }


    public static class GhostRiderRunnable implements Runnable {
        public AbstractHorse getHorse() {
            return horse;
        }

        public void setHorse(AbstractHorse horse) {
            this.horse = horse;
        }

        public AbstractHorse horse;
        public Player player;// todo 处理玩家下线

        public GhostRiderRunnable(Player player, AbstractHorse horse) {
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
                        Material blockMaterial = Material.NETHERITE_BLOCK;
                        GhostRiderSkillType ghostRiderSkillType = GhostRiderSkillType.NETHERRACK;
                        if (isFusion) {
                            blockMaterial = Material.OBSIDIAN;
                            ghostRiderSkillType = GhostRiderSkillType.OBSIDIAN;
                        }

                        GhostRiderBlockData ghostRiderBlockData = new GhostRiderBlockData();
                        if (location_GhostRiderBlockData.containsKey(blockLocation)) {
                            ghostRiderBlockData = location_GhostRiderBlockData.get(blockLocation);
                        }
                        boolean success = ghostRiderBlockData.put(playerUUID, GHOST_RIDER_NETHERRACK_DISAPPEAR_TIME, block.getState(), ghostRiderSkillType);
                        location_GhostRiderBlockData.put(blockLocation, ghostRiderBlockData);
                        if (success) {
                            block.setType(blockMaterial, false);
                        }
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




        public boolean put(UUID uuid, int offset, BlockState originalBlockState, GhostRiderSkillType ghostRiderSkillType) {
            if (!ghostRiderPlayerSkills.isEmpty()) {
                GhostRiderPlayerSkill ghostRiderPlayerSkill = ghostRiderPlayerSkills.getLast();
                if (ghostRiderPlayerSkill.getPlayerUUID().equals(uuid)) {
                    if (ghostRiderPlayerSkill.getGhostRiderSkillType().ordinal() <= ghostRiderSkillType.ordinal()) {
                        ghostRiderPlayerSkill.setTimeStamp(Bukkit.getCurrentTick() + offset);
                        ghostRiderPlayerSkill.setGhostRiderSkillType(ghostRiderSkillType);
                        return true;
                    }
                    else {
                        return false;  //就算时间更长的低级也return  //todo 搞和我的世界药水效果一样，高级的在上面 长时间低级的在下面但是也存在（比如你喝力量2 后喝无限试时间力量1），然后后来的在前面敌人的上面
                    }
                }
            }
            ghostRiderPlayerSkills.add(new GhostRiderPlayerSkill(uuid, Bukkit.getCurrentTick() + offset, originalBlockState, ghostRiderSkillType));
            return true;
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
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 0 , true, true));
                }
                else if (ghostRiderSkillType == GhostRiderSkillType.SOUL_SAND) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 1 , true, true));
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
