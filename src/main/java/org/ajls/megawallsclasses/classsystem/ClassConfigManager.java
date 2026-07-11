package org.ajls.megawallsclasses.classsystem;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ClassConfigManager {

    private static ClassConfigManager instance;

    private final JavaPlugin plugin;
    private final Map<Integer, ClassConfigEntry> cache = new HashMap<>();

    private ClassConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        reloadCache();
    }

    public static ClassConfigManager getInstance(JavaPlugin plugin) {
        if (instance == null) {
            instance = new ClassConfigManager(plugin);
        }
        return instance;
    }

    public void reloadFromDisk() {
        plugin.reloadConfig();
        reloadCache();
    }

    public void reloadCache() {
        cache.clear();

        FileConfiguration config = plugin.getConfig();
        ConfigurationSection classesSection =
                config.getConfigurationSection(ClassConfigKeys.CLASSES);

        if (classesSection == null) return;

        for (String key : classesSection.getKeys(false)) {
            try {
                int classId = Integer.parseInt(key);
                ClassConfigEntry entry = loadClassConfig(classesSection, key);
                cache.put(classId, entry);
            } catch (NumberFormatException ignored) {
            }
        }
    }

    private ClassConfigEntry loadClassConfig(ConfigurationSection classesSection, String key) {
        String basePath = key + ".";

        String name = classesSection.getString(
                basePath + ClassConfigKeys.NAMES + "." + ClassConfigKeys.NAME,
                "无职业"
        );

        String shortName = classesSection.getString(
                basePath + ClassConfigKeys.NAMES + "." + ClassConfigKeys.SHORT_NAME,
                "无职"
        );

        PotionConfig healthPotion = loadHealthPotionConfig(classesSection, basePath);
        PotionConfig speedPotion = loadSpeedPotionConfig(classesSection, basePath);

        return new ClassConfigEntry(name, shortName, healthPotion, speedPotion);
    }

    private PotionConfig loadHealthPotionConfig(ConfigurationSection classesSection, String basePath) {
        String potionPath = basePath
                + ClassConfigKeys.INVENTORY + "."
                + ClassConfigKeys.HEALTH_POTION + ".";

        ClassConfigEntry defaults = ClassConfigEntry.defaultEntry();
        PotionConfig defaultPotion = defaults.healthPotion();

        int amount = classesSection.getInt(
                potionPath + ClassConfigKeys.AMOUNT,
                defaultPotion.amount()
        );
        int stackAmount = classesSection.getInt(
                potionPath + ClassConfigKeys.STACK_AMOUNT,
                defaultPotion.stackAmount()
        );
        int hp = classesSection.getInt(
                potionPath + ClassConfigKeys.HP,
                defaultPotion.hp()
        );

        return PotionConfig.healthPotion(amount, stackAmount, hp);
    }

    private PotionConfig loadSpeedPotionConfig(ConfigurationSection classesSection, String basePath) {
        String potionPath = basePath
                + ClassConfigKeys.INVENTORY + "."
                + ClassConfigKeys.SPEED_POTION + ".";

        ClassConfigEntry defaults = ClassConfigEntry.defaultEntry();
        PotionConfig defaultPotion = defaults.speedPotion();

        int amount = classesSection.getInt(
                potionPath + ClassConfigKeys.AMOUNT,
                defaultPotion.amount()
        );
        int stackAmount = classesSection.getInt(
                potionPath + ClassConfigKeys.STACK_AMOUNT,
                defaultPotion.stackAmount()
        );
        int durationTicks = classesSection.getInt(
                potionPath + ClassConfigKeys.DURATION_TICKS,
                defaultPotion.durationTicks()
        );
        int amplifier = classesSection.getInt(
                potionPath + ClassConfigKeys.AMPLIFIER,
                defaultPotion.amplifier()
        );

        return PotionConfig.speedPotion(amount, stackAmount, durationTicks, amplifier);
    }

    private ClassConfigEntry getEntry(int classId) {
        ClassConfigEntry entry = cache.get(classId);
        if (entry != null) return entry;

        return ClassConfigEntry.defaultEntry();
    }





    public String getClassName(int classId) {
        return getEntry(classId).name();
    }

    public String getClassShortName(int classId) {
        return getEntry(classId).shortName();
    }

    public int getHealthPotionAmount(int classId) {
        return getEntry(classId).healthPotion().amount();
    }

    public int getHealthPotionHp(int classId) {
        return getEntry(classId).healthPotion().hp();
    }

    public int getHealthPotionStackAmount(int classId) {
        return getEntry(classId).healthPotion().stackAmount();
    }

    public int getSpeedPotionAmount(int classId) {
        return getEntry(classId).speedPotion().amount();
    }

    public int getSpeedPotionDurationTicks(int classId) {
        return getEntry(classId).speedPotion().durationTicks();
    }

    public int getSpeedPotionAmplifier(int classId) {
        return getEntry(classId).speedPotion().amplifier();
    }

    public int getSpeedPotionStackAmount(int classId) {
        return getEntry(classId).speedPotion().stackAmount();
    }


}
