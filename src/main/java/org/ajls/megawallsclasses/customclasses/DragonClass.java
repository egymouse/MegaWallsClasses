package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.advanced.BukkitTaskMap;
import org.ajls.lib.advanced.HaxhMap;
import org.ajls.megawallsclasses.EnergyAccumulate;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.MyListener;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static org.ajls.megawallsclasses.MegaWallsClasses.getPlugin;
import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;
import static org.bukkit.Bukkit.getServer;
import static org.bukkit.Bukkit.getWorld;

public class DragonClass extends AbstractCustomClass {
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
                // 检查实体是否还存在且不再着火
                if (victimPlayer.isValid() && victimPlayer.getFireTicks() <= 0) {
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



    public static HashMap<Location, Pair<BlockState, UUID>> netherrack_dragon = new HashMap<>();
    @Override
    public void activeSkill(Player player, MyListener.ActiveSkillTypeEnum activeSkillType) {
        Location location = player.getLocation();
        Block centerBlock = location.getBlock();

        int sideLength = 11;

        int radius = (sideLength - 1) / 2;
        // 预分配列表容量以优化性能[reference:3][reference:4]
        int totalBlocks = sideLength * sideLength * sideLength;
//        List<Block> blocks = new ArrayList<>(totalBlocks);

        // 2. 三重循环遍历相对偏移量 (dx, dy, dz)
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    // 3. 直接获取相对位置的 Block
                    Block relativeBlock = centerBlock.getRelative(dx, dy, dz);
//                    blocks.add(relativeBlock);
                    BlockState blockState = relativeBlock.getState();
                    Location relativeLocation = relativeBlock.getLocation();
                    Pair<BlockState, UUID> pair = Pair.of(blockState, player.getUniqueId());
                    netherrack_dragon.put(relativeBlock.getLocation(), pair);

                    relativeBlock.setType(Material.NETHERRACK, false);
                    BlockState netherrackBlockState = relativeBlock.getState();

                    BukkitScheduler scheduler = Bukkit.getScheduler();
                    scheduler.scheduleSyncDelayedTask(plugin, () -> {
//                        Block newBlock = location1.getBlock();

//                        BlockState relativeAfterBlockState = netherrack_dragon.get(relativeLocation).getLeft();

                        BlockState oldState = netherrackBlockState;

// 之后判断
                        BlockState newState = oldState.getBlock().getState();

                        boolean changed =
                                oldState.getType() != newState.getType()
                                        || !oldState.getBlockData().equals(newState.getBlockData());

                        if (changed) {
//                            System.out.println("BlockState 改变了");
                        }
                        else {
                            blockState.update(true);
                        }
                        netherrack_dragon.remove(relativeLocation);

//                            }
                    }, 40);
                }
            }
        }
//        return blocks;




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
}
