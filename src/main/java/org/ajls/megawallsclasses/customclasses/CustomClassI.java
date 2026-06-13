package org.ajls.megawallsclasses.customclasses;

import org.ajls.megawallsclasses.ClassEnum;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public interface CustomClassI {
    ClassEnum classEnum = null;  // 存储注入的枚举

    default void setClassEnum(ClassEnum classEnum) {
        this.classEnum = classEnum;
    }

    default ClassEnum getClassEnum() {
        return classEnum;
    }

    // 默认实现：通过枚举获取 classId，再到配置文件读取名称
    default String getClassName() {
        int classId = getClassEnum().classId;
        return ClassNamesConfig.getInstance(MegaWallsClasses.getPlugin())
                .getClassName(classId);
    }

    default String getClassSimpleName() {
        int classId = getClassEnum().classId;
        return ClassNamesConfig.getInstance(MegaWallsClasses.getPlugin())
                .getClassShortName(classId);
    }



//    default String getClassName() {
//        return "没有职业";
//    }
//
//    default String getClassSimpleName() {
//        return "无职";
//    }


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

//    default void onTridentDamage(Player player) {
//        //老版本溺尸王
//        //todo 写好老版本溺尸王 三叉戟靠忠诚，但是保留命令方块设计（或者加一个不能放置的屏障） 如果背包满也会自动进入屏障 然后触发主动6秒内打中人才行（非扔出），或者不触发满能量打中人也行
//    }

    // ===================== 新增：三叉戟发射事件方法 =====================
    // 默认空实现：其他职业完全不用管，只有溺尸王重写
    default void onProjectileLaunch(ProjectileLaunchEvent event) {
        // 普通职业：无任何逻辑
    }

    default void onProjectileHit(ProjectileHitEvent event, Player player) {}

//    default void onTridentDamage(EntityDamageByEntityEvent event) {
//
//    }

    default void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

    }


    default void onPlayerPickupArrow(PlayerPickupArrowEvent event) {

    }


}
