package org.ajls.megawallsclasses;

import org.ajls.megawallsclasses.customclasses.CustomClassI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum ClassEnum implements CustomClassI {
    ZOMBIE(1),
    HEROBRINE(2),
    SKELETON(3){
        @Override
        public void sendSkillReadyMessage(Player player) {
            // 职业3专属提示
            player.sendMessage(ChatColor.GREEN + "终极技能" + ChatColor.YELLOW + " 准备就绪 " + ChatColor.AQUA + "射箭自动触发");
        }
    },
    ENDERMAN(4) {
        @Override
        public void applySkillReadyEffect(Player player) {
            // 职业4专属回血效果
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
            player.sendMessage("末影人灵魂爆发回血");
        }
        // 提示用通用的，不用重写！
    },
    NULL(5),
    DREAD_LORD(6),
    ENTITY_303(7),
    CREEPER(8),
    UNDEAD_KNIGHT(9),
    DROWNKING(10),
    SPIDER(11),
    SHAMAN(12),
    SNOWMAN(13),
    MOLE(14),
    ELAINA(15),
    SQUID(18),
//    COW,
//    GOLEM,
    SKELETON_LORD(28),
    TRANSFIGURATION_MASTER(29),
    WARDEN(30);
//    NAC, //not a class

    public final int classId;

    ClassEnum(int classId) {
        this.classId = classId;
    }
}

