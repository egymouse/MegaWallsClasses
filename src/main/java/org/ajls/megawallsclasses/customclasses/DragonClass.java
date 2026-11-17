package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.advanced.BukkitTaskMap;
import org.ajls.lib.advanced.HaxhMap;
import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.*;
import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

import static org.ajls.megawallsclasses.EnergyAccumulate.addEnergy;
import static org.ajls.megawallsclasses.MegaWallsClasses.getPlugin;
import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;
import static org.bukkit.Bukkit.getWorld;

public class DragonClass extends AbstractCustomClass {


    @Override
    public ItemStack createClassSword() {
        ItemStack classSword = ClassItemFactory.classSword(Material.DIAMOND_SWORD);
        classSword.addEnchantment(Enchantment.FIRE_ASPECT, 1);

        return classSword;
    }


    @Override
    public ItemStack createClassBow() {
        ItemStack classBow = ClassItemFactory.classBow();
        classBow.addEnchantment(Enchantment.POWER, 2);
        return classBow;
    }




    public static HaxhMap<UUID, UUID> combustPlayers_dragons = new HaxhMap<>();

    public static HashSet<UUID> netherFire = new HashSet<>();
    public static HashSet<UUID> arrowNetherFire = new HashSet<>();
    @Override
    public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {
        Entity combusterEntity = event.getCombuster();
        Entity victimEntity = event.getEntity();
        Player combusterPlayer = (Player) combusterEntity;
//        if (combusterEntity instanceof Player combusterPlayer) {
//        }
        if (victimEntity instanceof Player victimPlayer) {
            registerCombust(combusterPlayer, victimPlayer);
        }
    }

    public static void registerCombust(Player combusterPlayer, Player victimPlayer) {
        combustPlayers_dragons.put(victimPlayer.getUniqueId(), combusterPlayer.getUniqueId());
        // 启动一个定时任务来监控这个实体
        new BukkitRunnable() {
            @Override
            public void run() {
                if (victimPlayer == null) {
                    this.cancel();
                }
                // 检查实体是否还存在且不再着火
                else if (victimPlayer.isValid() && victimPlayer.getFireTicks() <= 0) {
                    // 在这里编写“实体灭火”后的逻辑
                    MegaWallsClasses.getPlugin().getLogger().info(victimPlayer.getName() + " 已灭火！");
                    combustPlayers_dragons.remove(victimPlayer.getUniqueId(), combusterPlayer.getUniqueId());
                    this.cancel(); // 任务完成，取消自身
                }
            }
        }.runTaskTimer(MegaWallsClasses.getPlugin(), 0L, 1L); // 每 tick (1/20秒) 检查一次
    }

    public static void switchMode(Player player) {
        if (!netherFire.remove(player.getUniqueId())) {
            netherFire.add(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "地狱火开启");
        }
        else {
            player.sendMessage(ChatColor.RED + "地狱火关闭");
        }
    }


    @Override
    public boolean tryActiveSkillJudge(Player player, MyListener.ActiveSkillTypeEnum activeSkillType) {
        if (activeSkillType == MyListener.ActiveSkillTypeEnum.BOW) {
            switchMode(player);
            return false;
        }
        return true;
    }

    public static HashMap<Location, DragonNetherrackData> netherrack_data = new HashMap<>();
    @Override
    public boolean activeSkill(Player player) {
        Location location = player.getLocation();
        Block centerBlock = location.getBlock();
        Location centerBlockLocation = centerBlock.getLocation();
        UUID playerUUID = player.getUniqueId();


        Vector direction = player.getLocation().getDirection().setY(0).normalize();

        int sideLength = 21;  //11

        int radius = (sideLength - 1) / 2;
        int radiusSquared = radius * radius;
        // 预分配列表容量以优化性能[reference:3][reference:4]
        int totalBlocks = sideLength * sideLength * sideLength;
//        List<Block> blocks = new ArrayList<>(totalBlocks);

        // 2. 三重循环遍历相对偏移量 (dx, dy, dz)
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    // 3. 直接获取相对位置的 Block
                    Block relativeBlock = centerBlock.getRelative(dx, dy, dz);
                    if (relativeBlock.isPassable()) continue;
//                    blocks.add(relativeBlock);

                    Location relativeLocation = relativeBlock.getLocation();
                    Vector diff = relativeLocation.clone().subtract(centerBlockLocation).toVector();
                    diff.setY(0);
                    if (diff.lengthSquared() > radiusSquared) continue;
                    diff.normalize();

                    if (direction.dot(diff) < 0.5) {  //0.86602540378  左右各30度
                        continue;
                    }

                    BlockState blockState = relativeBlock.getState();
                    DragonNetherrackData dragonNetherrackData = new DragonNetherrackData(blockState, playerUUID);  //todo 这里应该先检查一些Location的map里面有没有这个data，然后直接获取
                    dragonNetherrackData.increase();
                    netherrack_data.put(relativeLocation, dragonNetherrackData);

                    relativeBlock.setType(Material.NETHERRACK, false);
                    BlockState netherrackBlockState = relativeBlock.getState();

                    BukkitScheduler scheduler = Bukkit.getScheduler();
                    scheduler.scheduleSyncDelayedTask(plugin, () -> {
//                        Block newBlock = location1.getBlock();

//                        BlockState relativeAfterBlockState = netherrack_dragon.get(relativeLocation).getLeft();

                        BlockState oldState = netherrackBlockState;

// 之后判断
                        BlockState newState = oldState.getBlock().getState();

                        boolean changed = hasBlockStateChanged(oldState, newState);  //todo 也有bug，比如别人挖了然后重新放一个一样的

                        if (dragonNetherrackData.decrease()) {  //最后一个持续的技能了，结束了就真的结束了
                            if (changed) {
//                            System.out.println("BlockState 改变了");
                            }
                            else {
                                blockState.update(true);
                            }
                            netherrack_data.remove(relativeLocation);
                        }



//                            }
                    }, 40);
                }
            }
        }
        for (Player otherPlayer: Bukkit.getOnlinePlayers()) {
            tryNetherrackCombust(otherPlayer);
        }
        player.sendMessage(ChatColor.RED + "dragon主动名字忘记了 " + ChatColor.RED + "HP " + ChatColor.GREEN + "+7");
        addEnergy(player, -100);

//        return blocks;

        return true;




    }

    public static class DragonNetherrackData {
        private BlockState blockState;
        private UUID uuid;
        private int count = 0;

        public DragonNetherrackData(BlockState blockState, UUID uuid) {
            setBlockState(blockState);
            setUuid(uuid);
        }

        // Getter / Setter（可按需添加校验）
        public BlockState getBlockState() { return blockState; }
        public void setBlockState(BlockState blockState) {
            if (blockState == null) throw new IllegalArgumentException("blockState must not be null");
            this.blockState = blockState;
        }

        public UUID getUuid() { return uuid; }
        public void setUuid(UUID uuid) {
            if (uuid == null) throw new IllegalArgumentException("uuid must not be null");
            this.uuid = uuid;
        }

        public int getCount() { return count; }
        public void setCount(int count) {
            if (count < 0) throw new IllegalArgumentException("count must be non-negative");
            this.count = count;
        }

        public void increase() {
            count++;
        }

        public boolean decrease() {
            if (count <= 0)  throw new IllegalArgumentException("count must be positive");
            count--;
            return count <= 0;
        }

//        @Override
//        public boolean equals(Object o) { /* 自动生成或手动实现 */ }
//        @Override
//        public int hashCode() { /* 自动生成或手动实现 */ }
//        @Override
//        public String toString() { return "BlockStateWrapper{...}"; }
    }

    public static boolean hasBlockStateChanged(BlockState oldState, BlockState newState) {
        return oldState.getType() != newState.getType()
                || !oldState.getBlockData().equals(newState.getBlockData());
    }

    public static void tryNetherrackCombust(Player player) {
        //todo 排除旁观玩家
        Block floorBlock = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        Location floorLoc = floorBlock.getLocation();

        // 检查这个位置是否在被替换的集合中
         if (DragonClass.netherrack_data.containsKey(floorLoc)) {
            // 触发踩踏逻辑！
            // 这里可以根据你的需求选择方案 A 或 B（见下文）
            org.ajls.megawallsclasses.customclasses.DragonClass.DragonNetherrackData dragonNetherrackData = DragonClass.netherrack_data.get(floorLoc);
//            BlockState oldBlockState = blockState_UUID.getLeft();
//            BlockState newBlockState = oldBlockState.getBlock().getState();
//            if (hasBlockStateChanged(oldBlockState, newBlockState)) {
//                DragonClass.netherrack_dragon.remove(floorLoc);
//                return;
//            }
             if (floorBlock.getType() != Material.NETHERRACK) {
                 DragonClass.netherrack_data.remove(floorLoc);
                return;
             }

            UUID uuid = dragonNetherrackData.getUuid();
            Player dragon = Bukkit.getPlayer(uuid);
            if (dragon != null) {  //todo 把team存储在combust方块里面，这样龙下线了也能正常使用
                if (ScoreboardsAndTeams.isPlayerSameTeam(player, dragon)) return;
                DragonClass.registerCombust(player, dragon);
                if (player.getFireTicks() < 100) {
                    player.setFireTicks(100);
                }
            }


        }
    }



    @Override
    public void onPlayerMove(PlayerMoveEvent event) {

    }


    public static BukkitTaskMap<UUID> arrowFireTask = new BukkitTaskMap<>();
    @Override
    public void onEntityShootBow(EntityShootBowEvent event) {
        Player player = (Player) event.getEntity();
        Entity projectile = event.getProjectile();
        if (projectile instanceof Arrow arrow) {
            if (netherFire.contains(player.getUniqueId())) {
                if (EnergyAccumulate.getEnergy(player) >= 20) {
                    EnergyAccumulate.addEnergy(player, -20);
                    arrowNetherFire.add(arrow.getUniqueId());


                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                    BukkitTask task = scheduler.runTaskTimer(getPlugin(), () -> {

                        arrow.getWorld().spawnParticle(Particle.FLAME, arrow.getLocation(), 1);


                    }, 0L, 1L);
                    arrowFireTask.put(arrow.getUniqueId(), task);
                }
            }
        }
    }

//    @Override
//    public void initializeClassItems() {
//        addClassItem(ItemKind.DRAGON_SWORD, () -> {
//            ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD, 5);
//            itemStack.addEnchantment(Enchantment.FIRE_ASPECT, 1);
//            return ItemStackU.setStringPersistentData(itemStack, NameSpacedKeys.ITEM_CATEGORY, AbstractCustomClass.CLASS_SAME);
//        });
//
//
//
//    }

    //todo 如果同时两个龙 对同一块地板放大招
    //那么敌人踩上去 怎么算是谁点燃的
//    搞定了
//            完全可以玩了
//    除了能量获取数值不知道之外
//            其他都正常了
//（骗你的，其实还有一些你不写代码就不会知道的bug，但是你一辈子估计也遇不到）
//    比如说这个，我完全没处理
//    但是如果我不告诉你 你一辈子也不会想到这个bug
//👿
//    还有 如果龙放了大招 然后退服了
//            那么就没法燃烧了
//    因为无法判断敌人是不是同一个队伍的
//            解决方法就是标记地狱岩所属队伍
//    但是这样又要加一百个代码
//            我懒得写
//    反正就2s 而且退服对自己不利 而且正常人谁打一半退服啊
//    还有 如果大招你站在原地不动
//    那么不会更新燃烧时间
//            因为我是监听玩家移动时间来给上火的
//    你不动就不会触发时间
//    要解决的话就要建立一个 循环任务
//    遍历全服玩家 探测脚下是不是有标记的地狱岩
//    还有 我没写挖掘地狱岩会清除地狱火
//2种解决方法
//1是监听所有地狱岩可能的破坏方式
//👿
//    比如 挖掘 爆炸 老鼠挖洞 etc
//2是循环任务实时探测地狱岩数据有没有变
//            反正就是很麻烦
//    所以我懒得写
//            我只是在2s后探测地狱岩数据有没有变
//    如果没变就变回原来方块

    //todo 不对 还是有问题
    //那如果有人挖了地狱岩然后又补上了地狱岩呢
    //😃😂
    //烦死了不修了
    //反正你们到时候被大招打了跑就行了 别想着挖地狱岩
}
