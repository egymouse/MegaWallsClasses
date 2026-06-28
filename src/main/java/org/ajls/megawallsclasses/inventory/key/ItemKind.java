package org.ajls.megawallsclasses.inventory.key;

import java.util.HashMap;
import java.util.Map;

public enum ItemKind {
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
