package org.ajls.megawallsclasses.classsystem;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ClassConfigManager {
    private static ClassConfigManager instance;
    private final JavaPlugin plugin;
    private final Map<Integer, ClassNameEntry> cache = new HashMap<>();

    private ClassConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        reloadCache();
//        if (instance == null) {
//            instance = this;
//        }
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
        var classNamesSection = config.getConfigurationSection("classes");
        if (classNamesSection == null) return;

        for (String key : classNamesSection.getKeys(false)) {
            try {
                int id = Integer.parseInt(key);
                String name = classNamesSection.getString(key + ".names.name");
                String shortName = classNamesSection.getString(key + ".names.short-name");
                if (name != null && shortName != null) {
                    cache.put(id, new ClassNameEntry(name, shortName));
                }
            } catch (NumberFormatException ignored) {}
        }
    }


    // 重载配置文件
    public void reload() {  //todo 看看服务器reload会发生什么，并把这个也注册进去
        cache.clear();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        var classNamesSection = config.getConfigurationSection("classes");
        if (classNamesSection == null) return;

        for (String key : classNamesSection.getKeys(false)) {
            try {
                int id = Integer.parseInt(key);
                String name = classNamesSection.getString(key + ".names.name");
                String shortName = classNamesSection.getString(key + ".names.short-name");
                if (name != null && shortName != null) {
                    cache.put(id, new ClassNameEntry(name, shortName));
                }
            } catch (NumberFormatException ignored) {}
        }
    }

    public String getClassName(int classId) {
        ClassNameEntry entry = cache.get(classId);
        return entry != null ? entry.name : "无职业";  //未知职业
    }

    public String getClassShortName(int classId) {
        ClassNameEntry entry = cache.get(classId);
        return entry != null ? entry.shortName : "无职";  //未知
    }

    private static class ClassNameEntry {
        final String name;
        final String shortName;
        ClassNameEntry(String name, String shortName) {
            this.name = name;
            this.shortName = shortName;
        }
    }
}