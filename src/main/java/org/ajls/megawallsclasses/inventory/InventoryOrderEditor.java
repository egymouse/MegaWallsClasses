package org.ajls.megawallsclasses.inventory;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.NameSpacedKeys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Function;

public class InventoryOrderEditor {

    private final LayoutRepository repository;
    private final LayoutResolver layoutResolver;
    private final ClassRegistry classRegistry;

    public Inventory createCommonEditor(Player player) {
        InventoryLayout layout = repository.loadCommonLayout(player);
        List<ItemKey> keys = List.of(
                ItemKey.TOOLKIT,
                ItemKey.COMPASS,
                ItemKey.PICKAXE,
                ItemKey.BLOCK_1,
                ItemKey.SQUID_POTION,
                ItemKey.SWORD,
                ItemKey.FOOD,
                ItemKey.BOW,
                ItemKey.HEAL_POTION_1,
                ItemKey.HEAL_POTION_2,
                ItemKey.SPEED_POTION_1,
                ItemKey.SPEED_POTION_2,
                ItemKey.ARROW,
                ItemKey.ENDER_CHEST,
                ItemKey.BLOCK_2,
                ItemKey.AXE,
                ItemKey.HAY_BLOCK,
                ItemKey.NETHER_STAR
        );

        return createEditorInventory(player, "Reorder inventory", keys, layout, key ->
                previewItemForCommon(key, player)
        );
    }

    public Inventory createClassEditor(Player player, int classId) {
        ClassKit kit = classRegistry.get(classId);
        InventoryLayout layout = layoutResolver.resolve(player, kit);

        return createEditorInventory(player, "ClassReorderInventory", kit.itemKeys(), layout, key ->
                kit.createItem(key, player)
        );
    }

    private Inventory createEditorInventory(
            Player player,
            String title,
            List<ItemKey> keys,
            InventoryLayout layout,
            Function<ItemKey, ItemStack> itemProvider
    ) {
        Inventory inv = Bukkit.createInventory(player, 45, title);

        for (ItemKey key : keys) {
            OptionalInt slotOpt = layout.getSlot(key);
            if (slotOpt.isEmpty()) continue;

            ItemStack item = itemProvider.apply(key);
            markItemKey(item, key);

            inv.setItem(slotOpt.getAsInt(), item);
        }

        inv.setItem(36, createResetButton());
        inv.setItem(40, createConfirmButton());

        return inv;
    }

    private void markItemKey(ItemStack item, ItemKey key) {
        ItemStackU.setStringPersistentData(item, NameSpacedKeys.ITEM_TYPE, key.id());
    }

    public InventoryLayout readLayoutFromEditorInventory(Inventory inventory) {
        InventoryLayout layout = new InventoryLayout();

        for (int slot = 0; slot < 36; slot++) {
            ItemStack item = inventory.getItem(slot);
            if (item == null || item.getType() == Material.AIR) continue;

            String keyId = ItemStackU.getStringPersistentData(item, NameSpacedKeys.ITEM_TYPE);
            if (keyId == null) continue;

            ItemKey key = ItemKey.fromId(keyId);
            layout.setSlot(key, slot);
        }

        return layout;
    }

}
