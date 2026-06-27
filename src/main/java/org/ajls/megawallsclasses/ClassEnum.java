package org.ajls.megawallsclasses;

import org.ajls.lib.utils.ScoreboardU;
import org.ajls.megawallsclasses.customclasses.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ClassEnum {
    //todo 还有我在像对于不同类型的职业（比如默认职业，搞笑职业，老版本职业）来分个类，但是classId就会出现断层，这怎么解决？ 可以增加Category字段，无论是int还是新的Enum都行 public Enum Category
    NONE(0, new NoneClass()),  //无职转生
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
    WARDEN(30),
//    NAC, //not a class


    DRAGON(311);  //todo 改成每个玩家一个职业instance 存储在Playerdata里面

    public final int classId;
    public final AbstractCustomClass customClass;

    ClassEnum(int classId, AbstractCustomClass abstractCustomClass) {
        this.classId = classId;
        this.customClass = abstractCustomClass;
        this.customClass.setClassEnum(this);  // 同样为默认实现注入
    }

    ClassEnum(int classId) {
        this(classId, new NoneClass());  //到时候可以给无职专门分配一个职业来玩玩
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
    public static AbstractCustomClass getJob(int classScore) {
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

    public static AbstractCustomClass getClassByPlayer(Player player) {
        int classScore = ScoreboardU.getScore(player, "class");
        return getJob(classScore);
    }



    public enum ClassMinecraftVersionEnum {  //职业要指定版本来正常伤害结算
        JAVA_OLD,
        JAVA_NEW,
        BEDROCK
    }

    public enum ClassCategoryEnum {
        EASE_CATION(114514),
        DO_MC_ER_NORMAL(200),
        DO_MC_ER_LEGENDARY(300),
        SELFMADE(0),
        FANMADE(100);

        ClassCategoryEnum(int offset) {
        }

    }

}

