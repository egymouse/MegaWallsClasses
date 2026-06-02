package org.ajls.megawallsclasses.customclasses;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.UUID;

import static org.ajls.megawallsclasses.ActiveSkills.activeTridents;
import static org.ajls.megawallsclasses.EnergyAccumulate.addEnergy;
import static org.ajls.megawallsclasses.EnergyAccumulate.shootEnergyAccumulate;
import static org.ajls.megawallsclasses.KillsManager.registerPlayerHit;

public interface CustomClassI {
    default void test() {

    }

    // ---------------- 抽离：职业专属提示（通用默认实现）----------------
    default void sendSkillReadyMessage(Player player) {
        // 原代码：非3职业的通用提示
        player.sendMessage(ChatColor.GREEN + "终极技能 " + ChatColor.YELLOW + "准备就绪");
    }

    // ---------------- 抽离：职业专属效果（通用默认实现：无效果）----------------
    default void applySkillReadyEffect(Player player) {
        // 普通职业：没有额外效果
        //末影人
    }

    default void onTridentHit(Player player) {
        //老版本溺尸王
        //todo 写好老版本溺尸王 三叉戟靠忠诚，但是保留命令方块设计（或者加一个不能放置的屏障） 如果背包满也会自动进入屏障 然后触发主动6秒内打中人才行（非扔出），或者不触发满能量打中人也行
    }

    // ===================== 新增：三叉戟发射事件方法 =====================
    // 默认空实现：其他职业完全不用管，只有溺尸王重写
    default void onTridentLaunch(ProjectileLaunchEvent event, Player player) {
        // 普通职业：无任何逻辑
    }

    default void onTridentHit(EntityDamageByEntityEvent event) {

    }

    default void onEntityDamageByEntity(EntityDamageByEntityEvent    event) {

    }
}
