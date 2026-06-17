package org.ajls.megawallsclasses.listeners;

import org.ajls.megawallsclasses.MegaWallsClasses;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BaseListener implements Listener {
    public BaseListener() {
        // 子类实例化时，自动将自己注册到插件管理器
        MegaWallsClasses.getPlugin().getServer().getPluginManager().registerEvents(this, MegaWallsClasses.getPlugin());
    }
}