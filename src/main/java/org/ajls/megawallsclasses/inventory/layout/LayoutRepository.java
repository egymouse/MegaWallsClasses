package org.ajls.megawallsclasses.inventory.layout;

import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.inventory.ItemKey;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

public class LayoutRepository {

    private final Configuration config;

    public LayoutRepository(Configuration config) {
        this.config = config;
    }

    public InventoryLayout loadCommonLayout(Player player) {
        String path = "inventory_layouts.players." + player.getUniqueId() + ".common";
        return loadLayout(path).orElse(DefaultLayouts.common());
    }

    public Optional<InventoryLayout> loadClassLayout(Player player, int classId) {
        String path = "inventory_layouts.players." + player.getUniqueId() + ".classes." + classId;
        return loadLayout(path);
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
            ItemKey key = ItemKey.fromId(keyId);
            int slot = section.getInt(keyId);
            layout.setSlot(key, slot);
        }

        return Optional.of(layout);
    }

    private void saveLayout(String path, InventoryLayout layout) {
        config.set(path, null);

        for (Map.Entry<ItemKey, Integer> entry : layout.asMap().entrySet()) {
            config.set(path + "." + entry.getKey().id(), entry.getValue());
        }

        MegaWallsClasses.plugin.saveConfig();
    }
}
