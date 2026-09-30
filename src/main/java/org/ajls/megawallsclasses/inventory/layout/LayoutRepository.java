package org.ajls.megawallsclasses.inventory.layout;

import org.ajls.lib.utils.ConfigU;
import org.ajls.megawallsclasses.ClassEnum;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.customclasses.AbstractCustomClass;
import org.ajls.megawallsclasses.inventory.key.ItemCount;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.ajls.megawallsclasses.inventory.key.LayoutKey;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class LayoutRepository {
    //用来对配置读取/写入的


    public static InventoryLayout loadCommonLayout(Player player) {
        String path = "inventory_layouts.players." + player.getUniqueId() + ".common";
        path = "custom_inventory_order." + player.getName();
        Optional<InventoryLayout> commonLayoutOptional = loadLayout(path);
        if (commonLayoutOptional.isPresent()) {
            return commonLayoutOptional.get();
        }
        InventoryLayout defaultInventoryLayout = ClassEnum.NONE.customClass.getDefaultInventoryLayout();
        return defaultInventoryLayout;
    }

    public static InventoryLayout loadClassLayout(Player player, int classId) {
        String path = "inventory_layouts.players." + player.getUniqueId() + ".classes." + classId;
        path = "class_inventory_order."  + player.getName() + "." + classId;



        Optional<InventoryLayout> optionalClassLayout = loadLayout(path);
        if (optionalClassLayout.isPresent()) {
            return optionalClassLayout.get();
        }

        InventoryLayout commonLayout = loadCommonLayout(player);
        AbstractCustomClass customClass = ClassEnum.getClassEnumByClassID(classId).customClass;

//        InventoryLayout defaultClassLayout = ClassEnum.getClassEnumByClassID(classId).customClass.getDefaultInventoryLayout();

        Map<Integer, ItemCount> commonLayoutMap = commonLayout.asMap();
//        Map<Integer, ItemCount> classLayoutMap = defaultClassLayout.asMap();



        for (Map.Entry<ItemKind, Integer> entry : customClass.classItemKind_count.entrySet()) {
            ItemKind itemKind = entry.getKey();
            int amount = entry.getValue();
            for (int i = 0; i < amount; i++) {
                int freeslot = AbstractCustomClass.findFirstFreeSlot(commonLayout);
                commonLayout.setSlot(freeslot, itemKind, -1);
            }
        }

        return commonLayout;









//        return loadLayout(path).orElse(ClassEnum.getClassEnumByClassID(classId).customClass.getDefaultInventoryLayout());
    }

    public static InventoryLayout potionCondense(InventoryLayout inventoryLayout) {
        Map<Integer, ItemCount> layoutMap = inventoryLayout.asMap();
        boolean speed = false;
        boolean health = false;
        for (Map.Entry<Integer, ItemCount> entry : new ArrayList<>(layoutMap.entrySet())) {  //防止玩家把血药和速度药拆开了
            int slot = entry.getKey();
            ItemCount itemCount = entry.getValue();
            ItemKind itemKind = itemCount.getKind();
            int count = itemCount.getCount();
            if (itemKind == ItemKind.SPEED_POTION) {
                if (speed) {
                    inventoryLayout.removeSlot(slot);
                }
                else {
                    ItemCount newItemCount = new ItemCount(itemKind, -1);
                    inventoryLayout.setSlot(slot, newItemCount);
                    speed = true;
                }

            }
            else if (itemKind == ItemKind.HEAL_POTION) {
                if (health) {
                    inventoryLayout.removeSlot(slot);
                }
                else {
                    ItemCount newItemCount = new ItemCount(itemKind, -1);
                    inventoryLayout.setSlot(slot, newItemCount);
                    health = true;
                }

            }

        }

        return inventoryLayout;
    }

    public static void saveCommonLayout(Player player, InventoryLayout layout) {
        String path = "inventory_layouts.players." + player.getUniqueId() + ".common";
        path = "custom_inventory_order." + player.getName();
        potionCondense(layout);
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



    private static Optional<InventoryLayout> loadLayout(String path) {
        Configuration config = MegaWallsClasses.getPlugin().getConfig();
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) {
            return Optional.empty();
        }

        InventoryLayout layout = new InventoryLayout();
        /*
        common:
            0:
                dsword: 1
         */

        for (String slotString : section.getKeys(false)) {
            int slot = Integer.parseInt(slotString);

            ConfigurationSection slotSection = section.getConfigurationSection(slotString);
            slotSection.getKeys(false).forEach(key -> {
                int count = slotSection.getInt(key);
                ItemKind itemKind = ItemKind.fromId(key);
//                    ConfigurationSection kindSection = slotSection.getConfigurationSection(key);
                layout.setSlot(slot, itemKind, count);

            });

        }

        return Optional.of(layout);
    }

    private static void saveLayout(String path, InventoryLayout layout) {
        Configuration config = MegaWallsClasses.getPlugin().getConfig();
        config.set(path, null);

        for (Map.Entry<Integer, ItemCount> entry : layout.asMap().entrySet()) {
            config.set(path + "." + entry.getKey() + "." + entry.getValue().getKind().id(), entry.getValue().getCount()); //0:axe:2

        }

        MegaWallsClasses.plugin.saveConfig();
    }

    private static void resetLayout(String path) {
        Configuration config = MegaWallsClasses.getPlugin().getConfig();
        ConfigU.remove(config, path);
    }

    public static InventoryLayout resolve(Player player, AbstractCustomClass customClass) {

        if (customClass == ClassEnum.NONE.customClass) {
            return loadCommonLayout(player);
        }
        return loadClassLayout(player, customClass.getClassEnum().classId);
    }
}
