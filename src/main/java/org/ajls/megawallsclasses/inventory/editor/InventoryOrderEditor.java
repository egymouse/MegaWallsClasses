package org.ajls.megawallsclasses.inventory.editor;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.NameSpacedKeys;
import org.ajls.megawallsclasses.customclasses.AbstractCustomClass;
import org.ajls.megawallsclasses.inventory.item.EditorPreviewItemFactory;
import org.ajls.megawallsclasses.inventory.key.LayoutKey;
import org.ajls.megawallsclasses.inventory.layout.InventoryLayout;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.OptionalInt;

public class InventoryOrderEditor {

    public Inventory createEditor(
            Player player,
            String title,
            List<LayoutKey> keys,
            InventoryLayout layout,
            ItemPreviewProvider previewProvider
    ) {
        Inventory inventory = Bukkit.createInventory(player, 45, title);

        for (LayoutKey key : keys) {
            OptionalInt slotOpt = layout.getSlot(key);
            if (!slotOpt.isPresent()) continue;

            ItemStack item = previewProvider.createPreviewItem(player, key);
            markLayoutKey(item, key);

            inventory.setItem(slotOpt.getAsInt(), item);
        }

        inventory.setItem(36, createResetButton());
        inventory.setItem(40, createConfirmButton());

        return inventory;
    }

    public Inventory createCommonEditor(Player player, InventoryLayout layout, List<LayoutKey> commonKeys) {
        return createEditor(
                player,
                "Reorder inventory",
                commonKeys,
                layout,
                (p, key) -> EditorPreviewItemFactory.commonPreview(p, key)
        );
    }

    public Inventory createClassEditor(Player player, AbstractCustomClass customClass, InventoryLayout layout) {
        return createEditor(
                player,
                "ClassReorderInventory",
                customClass.getDefaultLayoutKeys(),
                layout,
                customClass::createInventoryItem
        );
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
