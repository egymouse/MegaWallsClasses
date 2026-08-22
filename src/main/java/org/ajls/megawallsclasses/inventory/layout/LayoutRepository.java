package org.ajls.megawallsclasses.inventory.layout;

import org.ajls.megawallsclasses.ClassEnum;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.inventory.key.LayoutKey;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

public class LayoutRepository {
    //用来对配置读取/写入的

    private final Configuration config;

    public LayoutRepository(Configuration config) {  //todo 把这里的全部看一遍
        this.config = config;
    }

    public InventoryLayout loadCommonLayout(Player player) {
        String path = "inventory_layouts.players." + player.getUniqueId() + ".common";
        path = "custom_inventory_order." + player.getName();
        return loadLayout(path).orElse(ClassEnum.NONE.customClass.getDefaultInventoryLayout());
    }

    public InventoryLayout loadClassLayout(Player player, int classId) {
        String path = "inventory_layouts.players." + player.getUniqueId() + ".classes." + classId;
        path = "class_inventory_order."  + player.getName() + "." + classId;
        return loadLayout(path).orElse(ClassEnum.getClassEnumByClassID(classId).customClass.getDefaultInventoryLayout());
    }

    public void saveCommonLayout(Player player, InventoryLayout layout) {
        String path = "inventory_layouts.players." + player.getUniqueId() + ".common";
        saveLayout(path, layout);
    }

    public void saveClassLayout(Player player, int classId, InventoryLayout layout) {
        String path = "inventory_layouts.players." + player.getUniqueId() + ".classes." + classId;
        saveLayout(path, layout);
    }

    private Optional<InventoryLayout> loadLayout(String path) {
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) {
            return Optional.empty();
        }

        InventoryLayout layout = new InventoryLayout();

        for (String keyId : section.getKeys(false)) {
            LayoutKey key = LayoutKey.fromId(keyId);
            int slot = section.getInt(keyId);
            layout.setSlot(key, slot);
        }

        return Optional.of(layout);
    }

    private void saveLayout(String path, InventoryLayout layout) {
        config.set(path, null);

        for (Map.Entry<LayoutKey, Integer> entry : layout.asMap().entrySet()) {
            config.set(path + "." + entry.getKey().id(), entry.getValue());
        }

        MegaWallsClasses.plugin.saveConfig();
    }
}
