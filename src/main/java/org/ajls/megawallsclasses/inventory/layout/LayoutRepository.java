package org.ajls.megawallsclasses.inventory.layout;

import org.ajls.lib.utils.ConfigU;
import org.ajls.megawallsclasses.ClassEnum;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.inventory.key.ItemCount;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.ajls.megawallsclasses.inventory.key.LayoutKey;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class LayoutRepository {
    //用来对配置读取/写入的

//    private static final Configuration config;

//    public LayoutRepository(Configuration config) {
//        this.config = config;
//    }

//    public void initialize(Configuration config) {
//
//    }

    public static InventoryLayout loadCommonLayout(Player player) {
        String path = "inventory_layouts.players." + player.getUniqueId() + ".common";
        path = "custom_inventory_order." + player.getName();
        return loadLayout(path, true).orElse(ClassEnum.NONE.customClass.getDefaultInventoryLayout());
    }

    public static InventoryLayout loadClassLayout(Player player, int classId) {
        String path = "inventory_layouts.players." + player.getUniqueId() + ".classes." + classId;
        path = "class_inventory_order."  + player.getName() + "." + classId;


        InventoryLayout commonLayout = loadCommonLayout(player);  //todo  想办法把class的给注入进common
        return loadLayout(path, false).orElse(ClassEnum.getClassEnumByClassID(classId).customClass.getDefaultInventoryLayout());
    }

    public static void saveCommonLayout(Player player, InventoryLayout layout) {
        String path = "inventory_layouts.players." + player.getUniqueId() + ".common";
        path = "custom_inventory_order." + player.getName();
        saveLayout(path, layout);
    }

    public static void saveClassLayout(Player player, int classId, InventoryLayout layout) {
        String path = "inventory_layouts.players." + player.getUniqueId() + ".classes." + classId;
        path = "class_inventory_order."  + player.getName() + "." + classId;
        saveLayout(path, layout);
    }


    public static void resetCommonLayout(Player player) {
        String path = "inventory_layouts.players." + player.getUniqueId() + ".common";
        path = "custom_inventory_order." + player.getName();
        resetLayout(path);
    }

    public static void resetClassLayout(Player player, int classId) {
        String path = "inventory_layouts.players." + player.getUniqueId() + ".classes." + classId;
        path = "class_inventory_order."  + player.getName() + "." + classId;
        resetLayout(path);
    }



    private static Optional<InventoryLayout> loadLayout(String path, boolean isCommon) {
        Configuration config = MegaWallsClasses.getPlugin().getConfig();
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) {
            return Optional.empty();
        }

        InventoryLayout layout = new InventoryLayout();

        for (String slotString : section.getKeys(false)) {
            int slot = Integer.parseInt(slotString);
//            config.get(path + "." + slotString, ItemCount.class);  //还能serailize?
            if (isCommon == section.contains(slotString)) {
                Bukkit.broadcastMessage("isCommon == section.contains(slotString)");
            }

            if (isCommon) {
                String itemKindString = config.getString(path + "." + slotString);
                itemKindString = section.getString(slotString);  //理论上是一样的
                ItemKind itemKind = ItemKind.valueOf(itemKindString);
                layout.setSlot(slot, itemKind);
            }
            else {
                ConfigurationSection slotSection = section.getConfigurationSection(slotString);
                slotSection.getKeys(false).forEach(key -> {
                    int count = slotSection.getInt(key);
                    ItemKind itemKind = ItemKind.valueOf(key);
//                    ConfigurationSection kindSection = slotSection.getConfigurationSection(key);
                    layout.setSlot(slot, itemKind, count);

                });
            }


//            LayoutKey key = LayoutKey.fromId(keyId);
//            int slot = section.getInt(keyId);
//            layout.setSlot(key, slot);
        }

        return Optional.of(layout);
    }

    private static void saveLayout(String path, InventoryLayout layout) {
        AtomicBoolean isCommon = new AtomicBoolean(true);
        layout.asMap().forEach((key, value) -> {
            if (value.getCount() != -1) {
                isCommon.set(false);
            }
        });
        Configuration config = MegaWallsClasses.getPlugin().getConfig();
        config.set(path, null);

        for (Map.Entry<Integer, ItemCount> entry : layout.asMap().entrySet()) {
            if (isCommon.get()) {
                config.set(path + "." + entry.getKey(), entry.getValue().getKind()); //0:axe
            }
            else {
                config.set(path + "." + entry.getKey() + "." + entry.getValue().getKind(), entry.getValue().getCount()); //0:axe:2
            }

        }

        MegaWallsClasses.plugin.saveConfig();
    }

    private static void resetLayout(String path) {
        Configuration config = MegaWallsClasses.getPlugin().getConfig();
        ConfigU.remove(config, path);
    }
}
