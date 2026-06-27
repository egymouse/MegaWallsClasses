package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.advanced.HaxhMap;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.MyListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.UUID;

public class DragonClass extends AbstractCustomClass {
    public HaxhMap<UUID, UUID> combustPlayers_dragons = new HaxhMap<>();

    public HashSet<UUID> netherFire = new HashSet<>();
    @Override
    public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {
        Entity combusterEntity = event.getCombuster();
        Entity victimEntity = event.getEntity();
        Player combusterPlayer = (Player) combusterEntity;
//        if (combusterEntity instanceof Player combusterPlayer) {
//        }
        if (victimEntity instanceof Player victimPlayer) {
            combustPlayers_dragons.put(victimEntity.getUniqueId(), combusterPlayer.getUniqueId());
            // 启动一个定时任务来监控这个实体
            new BukkitRunnable() {
                @Override
                public void run() {
                    // 检查实体是否还存在且不再着火
                    if (victimPlayer.isValid() && victimPlayer.getFireTicks() <= 0) {
                        // 在这里编写“实体灭火”后的逻辑
                        MegaWallsClasses.getPlugin().getLogger().info(victimPlayer.getName() + " 已灭火！");
                        combustPlayers_dragons.remove(victimEntity.getUniqueId(), combusterPlayer.getUniqueId());
                        this.cancel(); // 任务完成，取消自身
                    }
                }
            }.runTaskTimer(MegaWallsClasses.getPlugin(), 0L, 1L); // 每 tick (1/20秒) 检查一次
        }
    }

    @Override
    public void activeSkill(Player player, MyListener.ActiveSkillTypeEnum activeSkillType) {
        if (activeSkillType == MyListener.ActiveSkillTypeEnum.BOW) {

        }
    }
}
