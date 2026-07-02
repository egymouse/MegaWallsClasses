package org.ajls.megawallsclasses.inventory.key;

import java.util.HashMap;
import java.util.Map;

public enum ItemKind {
        /*todo 血药，速度药改成一个就行，最好备战未来适配多个重复物品（比如说两组血药）
    fromid改成hashmap存储
    default layouts的layout如何复制？最好用static，在加载时执行一次就好。
    职业的default layouts可以通过寻找可用最小索引添加
    classKit的话 目前我是有自己的职业管理系统 看能不能集成 包括registry
    职业剑，弓，速度药，血药需要默认值 比如大部分职业血药都是两瓶16hp
    config有必要改成markdirty然后只在服务器启动/停止或者每隔几分钟保存config吗
    Cannot resolve method 'or' in 'OptionalInt' 在laayout resolver的resolve
    InventoryOrderEditor那块没怎么听懂，附上MyListener原本的实现，同时给出自己玩家打开物品栏用state machine管理的构想，就不用title来管理了



    最后包结构我也不会，看看文件放在哪个包下好
     */

    /*
    LayoutKey并没copy，会不会有什么问题
    这default layout的common 只有ClassDefaultLayoutResolver getDefaultLayout才修改，其他都是只读，copy会不会浪费性能
    如果class想要自定义职业默认layout特殊物品的位置呢？ 估计不会
    getDefaultLayoutKeys能不能在override执行增量操作 可以call super就行

    血药，速度药这些信息配置在config里面
    需要config结构重置 包括之前的职业名称

    把itemFactory在生成key的时候就绑定在一起，减少物品反复生成和if判断

    class sword和class bow是先获取白板，然后再添加新东西






     */
    TOOLKIT("toolkit"),
    COMPASS("compass"),
    PICKAXE("pickaxe"),
    ENDER_CHEST("ender_chest"),
    NETHER_STAR("nether_star"),

    SWORD("sword"),
    BOW("bow"),
    ARROW("arrow"),

    BLOCK("block"),
    FOOD("food"),
    AXE("axe"),
    HAY_BLOCK("hay_block"),

    SPEED_POTION("speed_potion"),
    HEAL_POTION("heal_potion"),

    SQUID_POTION("squid_potion"),

    // 职业特殊物品
    SNOWMAN_BLOOD_BLOCK("snowman_blood_block"),
    MOLE_PIE("mole_pie"),
    MOLE_BEEF("mole_beef"),
    ELAINA_GOLDEN_CARROT("elaina_golden_carrot"),
    ELAINA_IRON_SWORD("elaina_iron_sword"),
    ELAINA_POTION("elaina_potion"),
    WARDEN_SENSOR("warden_sensor");

    private final String id;

    ItemKind(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    private static final Map<String, ItemKind> BY_ID = new HashMap<>();

    static {
        for (ItemKind kind : values()) {
            BY_ID.put(kind.id, kind);
        }
    }

    public static ItemKind fromId(String id) {
        ItemKind kind = BY_ID.get(id);
        if (kind == null) {
            throw new IllegalArgumentException("Unknown item kind: " + id);
        }
        return kind;
    }
}
