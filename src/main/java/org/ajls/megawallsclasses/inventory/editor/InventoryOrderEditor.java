package org.ajls.megawallsclasses.inventory.editor;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.ClassEnum;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.NameSpacedKeys;
import org.ajls.megawallsclasses.customclasses.AbstractCustomClass;
import org.ajls.megawallsclasses.inventory.key.LayoutKey;
import org.ajls.megawallsclasses.inventory.layout.InventoryLayout;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class InventoryOrderEditor {

    public Inventory createEditor(
            Player player,
            String title,
            AbstractCustomClass abstractCustomClass
    ) {


        InventoryLayout inventoryLayout = MegaWallsClasses.getPlugin().getLayoutResolver().resolve(player, abstractCustomClass);

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



    public Inventory createCommonEditor(Player player) {
        return createEditor(
                player,
                "Reorder inventory",
                ClassEnum.NONE.customClass
        );

    }

    public Inventory createClassEditor(Player player, AbstractCustomClass customClass) {
        return createEditor(
                player,
                "ClassReorderInventory",
                customClass
        );
    }

    public void saveCommonEditor(Player player, Inventory inventory) {
        InventoryLayout inventoryLayout = readLayoutFromInventory(inventory);
        MegaWallsClasses.getPlugin().getLayoutRepository().saveCommonLayout(player, inventoryLayout);
    }

    public void saveClassEditor(Player player, int classID, Inventory inventory) {
        InventoryLayout inventoryLayout = readLayoutFromInventory(inventory);
        MegaWallsClasses.getPlugin().getLayoutRepository().saveClassLayout(player, classID, inventoryLayout);
    }

    public InventoryLayout readLayoutFromInventory(Inventory inventory) {
        InventoryLayout layout = new InventoryLayout();

        for (int slot = 0; slot < 36; slot++) {
            ItemStack item = inventory.getItem(slot);
            if (item == null || item.getType() == Material.AIR) continue;

            String keyId = ItemStackU.getStringPersistentData(item, NameSpacedKeys.ITEM_TYPE);
            if (keyId == null) continue;

            LayoutKey key = LayoutKey.fromId(keyId);
            layout.setSlot(key, slot);
        }

        return layout;
    }

    private void markLayoutKey(ItemStack item, LayoutKey key) {
        ItemStackU.setStringPersistentData(item, NameSpacedKeys.ITEM_TYPE, key.id());
    }

    private ItemStack createConfirmButton() {
        ItemStack item = new ItemStack(Material.GREEN_CONCRETE);
        ItemStackU.setDisplayName(item, "confirm");
        return item;
    }

    private ItemStack createResetButton() {
        ItemStack item = new ItemStack(Material.YELLOW_CONCRETE);
        ItemStackU.setDisplayName(item, "reset_to_default");
        return item;
    }

}
