package org.ajls.megawallsclasses.inventory;

public enum ItemKey {
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

    public static ItemKey fromId(String id) {  //todo 改成hashmap
        for (ItemKey key : values()) {
            if (key.id.equals(id)) return key;
        }
        throw new IllegalArgumentException("Unknown item key: " + id);
    }
}
