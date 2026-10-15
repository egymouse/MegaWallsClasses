package org.ajls.megawallsclasses.classsystem;

public final class ClassConfigKeys {

    private ClassConfigKeys() {
    }

    public static final String CLASSES = "classes";
    public static final String NAMES = "names";
    public static final String NAME = "name";
    public static final String SHORT_NAME = "short-name";
    public static final String INVENTORY = "inventory";

    // 注意：这里保留你现在 config 里的 health_potion
    public static final String HEALTH_POTION = "health_potion";
    public static final String SPEED_POTION = "speed_potion";
    public static final String CUSTOM_POTION = "custom_potion";

    public static final String AMOUNT = "amount";  //一组多少个
    public static final String TOTAL_STACKS = "total_stacks";  // 多少组，都是1
    public static final String HP = "hp";
    public static final String DURATION_TICKS = "duration_ticks";
    public static final String AMPLIFIER = "amplifier";
}
