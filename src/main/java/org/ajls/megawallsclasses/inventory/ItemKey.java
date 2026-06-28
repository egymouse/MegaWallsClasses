package org.ajls.megawallsclasses.inventory;

public enum ItemKey {
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

    TOOLKIT("toolkit"),
    COMPASS("compass"),
    PICKAXE("pickaxe"),
    ENDER_CHEST("ender_chest"),
    NETHER_STAR("nether_star"),

    SWORD("sword"),
    BOW("bow"),
    ARROW("arrow"),

    BLOCK_1("block_1"),
    BLOCK_2("block_2"),

    FOOD("food"),
    AXE("axe"),
    HAY_BLOCK("hay_block"),

    SPEED_POTION_1("speed_potion_1"),
    SPEED_POTION_2("speed_potion_2"),
    HEAL_POTION_1("heal_potion_1"),
    HEAL_POTION_2("heal_potion_2"),

    SQUID_POTION("squid_potion"),

    // 职业特殊物品
    SNOWMAN_BLOOD_BLOCK("snowman_blood_block"),
    MOLE_PIE("mole_pie"),
    MOLE_BEEF("mole_beef"),
    ELAINA_GOLDEN_CARROT("elaina_golden_carrot"),
    ELAINA_IRON_SWORD("elaina_iron_sword"),
    ELAINA_POTION_1("elaina_potion_1"),
    WARDEN_SENSOR("warden_sensor");

    private final String id;

    ItemKey(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public static ItemKey fromId(String id) {
        for (ItemKey key : values()) {
            if (key.id.equals(id)) return key;
        }
        throw new IllegalArgumentException("Unknown item key: " + id);
    }
}
