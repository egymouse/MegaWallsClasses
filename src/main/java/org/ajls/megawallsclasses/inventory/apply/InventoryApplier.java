package org.ajls.megawallsclasses.inventory.apply;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.ClassEnum;
import org.ajls.megawallsclasses.ClassU;
import org.ajls.megawallsclasses.NameSpacedKeys;
import org.ajls.megawallsclasses.customclasses.AbstractCustomClass;
import org.ajls.megawallsclasses.inventory.key.ItemCount;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.ajls.megawallsclasses.inventory.layout.InventoryLayout;
import org.ajls.megawallsclasses.inventory.layout.LayoutRepository;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InventoryApplier {

    public static void apply(Player player) {
        AbstractCustomClass customClass = ClassEnum.getCustomClassByPlayer(player);
        apply(player, customClass);
    }

    public static void apply(Player player, AbstractCustomClass customClass) {
//        clearStorageContents(player);  //自己手动清理

        boolean isTF = ClassU.isTransformationMaster(player);
        Inventory playerInventory = player.getInventory();


        InventoryLayout layout = LayoutRepository.resolve(player, customClass);
        Map<Integer, ItemCount> layoutMap = layout.asMap();
        boolean TFImitate = isTF && customClass.getClassEnum() != ClassEnum.TRANSFIGURATION_MASTER;
        if (!TFImitate) {  //todo 区分职业的魔女药和鱿鱼药 与 杀人的魔女药和自带鱿鱼药
            for (int i2 = 0; i2 < playerInventory.getSize(); i2++) {
                ItemStack playerItemStack = playerInventory.getItem(i2);
                String itemCategory = ItemStackU.getStringPersistentData(playerItemStack, NameSpacedKeys.ITEM_CATEGORY);
                if (itemCategory != null) {
                    if (itemCategory.equals(AbstractCustomClass.CLASS_DIFF)) {
                        playerInventory.setItem(i2, null);
                    }
                }
            }
        }
        for (Map.Entry<Integer, ItemCount> entry : layoutMap.entrySet()) {
            Integer slot = entry.getKey();
            ItemCount itemCount = entry.getValue();
            ItemKind itemKind = itemCount.getKind();
            int count = itemCount.getCount();

            ItemStack itemStack = customClass.createInventoryItem(itemKind, count);
            String itemType = ItemStackU.getStringPersistentData(itemStack, NameSpacedKeys.ITEM_TYPE);
            String itemCategory = ItemStackU.getStringPersistentData(itemStack, NameSpacedKeys.ITEM_CATEGORY);
            if (TFImitate) {
                if (AbstractCustomClass.CLASS_SAME.equals(itemCategory)) {
                    boolean findSameItemType = false;
                    for (int i2 = 0; i2 < playerInventory.getSize(); i2++) {
                        ItemStack playerItemStack = playerInventory.getItem(i2);
                        String playerItemType = ItemStackU.getStringPersistentData(playerItemStack, NameSpacedKeys.ITEM_TYPE);
                        if (playerItemType != null) {
                            if (playerItemType.equals(itemType)) {
                                playerInventory.setItem(i2, itemStack);
                                findSameItemType = true;
                                break;
                            }
                        }
                    }
                    if (!findSameItemType) {
                        addItemToSlot(player, itemStack, slot);
                    }

                }
                else if (AbstractCustomClass.CLASS_DIFF.equals(itemCategory)) {
                    addItemToSlot(player, itemStack, slot);
                }
            }
            else {
                if (itemCategory == null) {
                    ItemStackU.removePersistentData(itemStack, NameSpacedKeys.ITEM_TYPE);
                }
                playerInventory.setItem(slot, itemStack);
            }

        }

    }


    public static void addItemToSlot(Player player, ItemStack itemStack, int slot) {
        Inventory playerInventory = player.getInventory();
        ItemStack movedItem = playerInventory.getItem(slot);
        playerInventory.setItem(slot, itemStack);
        if (movedItem != null) {
            HashMap<Integer, ItemStack> stack = playerInventory.addItem(movedItem);
            for (ItemStack extraItemStack : stack.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), extraItemStack);
            }
        }
    }

    public static void apply(Player player, ClassEnum classEnum) {
        apply(player, classEnum.customClass);
    }

//    private static void clearStorageContents(Player player) {
//        for (int i = 0; i < 36; i++) {
//            player.getInventory().setItem(i, null);
//        }
//    }

}
