package org.ajls.megawallsclasses.inventory.editor;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.ClassEnum;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.NameSpacedKeys;
import org.ajls.megawallsclasses.customclasses.AbstractCustomClass;
import org.ajls.megawallsclasses.inventory.key.LayoutKey;
import org.ajls.megawallsclasses.inventory.layout.InventoryLayout;
import org.ajls.megawallsclasses.inventory.layout.LayoutRepository;
import org.ajls.megawallsclasses.inventory.layout.LayoutResolver;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;

public class InventoryOrderEditor {

    public static Inventory createEditor(
            Player player,
            String title,
            AbstractCustomClass abstractCustomClass
    ) {


        InventoryLayout inventoryLayout = LayoutResolver.resolve(player, abstractCustomClass);

        Map<LayoutKey, Integer> inventoryLayoutMap = inventoryLayout.asMap();
        Inventory inventory = Bukkit.createInventory(player, 45, title);

        for (LayoutKey key : inventoryLayoutMap.keySet()) {
            int index = inventoryLayoutMap.get(key);
            ItemStack itemStack = abstractCustomClass.createInventoryItem(key);

            inventory.setItem(index, itemStack);
        }

        inventory.setItem(36, createResetButton());
        inventory.setItem(40, createConfirmButton());

        return inventory;
    }



    public static Inventory createCommonEditor(Player player) {
        return createEditor(
                player,
                "Reorder inventory",
                ClassEnum.NONE.customClass
        );

    }

    public static Inventory createClassEditor(Player player, AbstractCustomClass customClass) {
        return createEditor(
                player,
                "ClassReorderInventory",
                customClass
        );
    }

    public static void saveCommonEditor(Player player, Inventory inventory) {
        InventoryLayout inventoryLayout = readLayoutFromInventory(inventory);
        LayoutRepository.saveCommonLayout(player, inventoryLayout);
    }

    public static void saveClassEditor(Player player, Inventory inventory, int classID) {
        InventoryLayout inventoryLayout = readLayoutFromInventory(inventory);
        LayoutRepository.saveClassLayout(player, classID, inventoryLayout);
    }

    public static void resetClassEditor(Player player, int classIndex) {
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        configuration.set("class_inventory_order." + playerName + "." + classIndex, null);
        MegaWallsClasses.plugin.saveConfig();
    }

    public static void resetCommonEditor(Player player) {
        Configuration configuration = plugin.getConfig();
        String playerName = player.getName();
        configuration.set("custom_inventory_order." + playerName, null);
        MegaWallsClasses.plugin.saveConfig();
    }

    public static InventoryLayout readLayoutFromInventory(Inventory inventory) {
        InventoryLayout layout = new InventoryLayout();

        for (int slot = 0; slot < 36; slot++) {
            ItemStack item = inventory.getItem(slot);
            if (item == null || item.getType() == Material.AIR) continue;

            String keyId = ItemStackU.getStringPersistentData(item, NameSpacedKeys.ITEM_TYPE);
            if (keyId == null) continue;

            LayoutKey key = LayoutKey.fromId(keyId);  //todo 在这里的id只有Kind没index 要思考要不要把index也注入 还是说根据kind自动分配index  改成item: slot:amount的形式 invlayout也要改
            layout.setSlot(key, slot);
        }

        return layout;
    }

    private static void markLayoutKey(ItemStack item, LayoutKey key) {
        ItemStackU.setStringPersistentData(item, NameSpacedKeys.ITEM_TYPE, key.id());
    }

    private static ItemStack createConfirmButton() {
        ItemStack item = new ItemStack(Material.GREEN_CONCRETE);
        ItemStackU.setDisplayName(item, "confirm");
        return item;
    }

    private static ItemStack createResetButton() {
        ItemStack item = new ItemStack(Material.YELLOW_CONCRETE);
        ItemStackU.setDisplayName(item, "reset_to_default");
        return item;
    }

}
