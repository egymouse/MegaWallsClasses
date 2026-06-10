package org.ajls.megawallsclasses;

import org.ajls.lib.utils.ScoreboardU;
import org.ajls.megawallsclasses.customclasses.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ClassEnum {
    NONE(0),  //无职转生
    ZOMBIE(1, new ZombieClass()),
    HEROBRINE(2, new HerobrineClass()),
    SKELETON(3, new SkeletonClass()),
    ENDERMAN(4, new EndermanClass()),
    NULL(5, new NullClass()),
    DREAD_LORD(6, new DreadlordClass()),
    ENTITY_303(7, new Entity303Class()),
    CREEPER(8, new CreeperClass()),
    UNDEAD_KNIGHT(9, new UndeadKnightClass()),
    DROWNKING(10, new DrownkingClass()), //DrownkingOldClass()
    SPIDER(11, new SpiderClass()),
    SHAMAN(12, new ShamanClass()),
    SNOWMAN(13, new SnowmanClass()),
    MOLE(14, new MoleClass()),
    ELAINA(15, new ElainaClass()),
    SQUID(18, new SquidClass()),


//    COW,
//    GOLEM,
    SKELETON_LORD(28),
    TRANSFIGURATION_MASTER(29),
    WARDEN(30);
//    NAC, //not a class

    public final int classId;
    public final CustomClassI customClass;

    ClassEnum(int classId, CustomClassI customClassI) {
        this.classId = classId;
        this.customClass = customClassI;
    }

    ClassEnum(int classId) {
        this.classId = classId;
        this.customClass = new CustomClass();
    }




    // ===================== 核心：根据ID查找 =====================
    // 1. 静态Map缓存：类加载时就把ID和枚举绑定好，查找速度极快（O(1)）
    private static final Map<Integer, ClassEnum> JOB_CACHE;
    static {
        JOB_CACHE = Arrays.stream(values())
                .collect(Collectors.toMap(
                        job -> job.classId,   // Key：职业ID
                        job -> job          // Value：枚举对象
                ));
    }

    /**
     * 根据 jobId 获取职业枚举（推荐使用！）
     * @param jobId 职业ID
     * @return 对应的枚举，找不到返回null
     */
    public static ClassEnum getByJobId(int jobId) {
        ClassEnum classEnum = JOB_CACHE.get(jobId);
        if (classEnum == null) {
//            Bukkit.broadcastMessage("有人无职转生了bruh");
            Bukkit.broadcastMessage("有人正在尝试新职业");
//            throw new IllegalArgumentException("无效的职业ID：" + jobId);
            return NONE;
        }
        return classEnum;
    }

    // 核心：通过积分榜分数获取职业（唯一判断逻辑）
    public static CustomClassI getJob(int classScore) {
//        for (ClassEnum factory : values()) {
//            if (factory.classId == classScore) {
//                return factory.customClass;
//            }
//        }
//        return WARRIOR.job; // 默认职业
//
//
        ClassEnum classEnum = getByJobId(classScore);
        return classEnum.customClass;
    }

    public static CustomClassI getClassByPlayer(Player player) {
        int classScore = ScoreboardU.getScore(player, "class");
        return getJob(classScore);
    }
}

