package org.ajls.megawallsclasses.inventory.apply;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.ClassEnum;
import org.ajls.megawallsclasses.ClassU;
import org.ajls.megawallsclasses.NameSpacedKeys;
import org.ajls.megawallsclasses.customclasses.AbstractCustomClass;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.ajls.megawallsclasses.inventory.layout.InventoryLayout;
import org.ajls.megawallsclasses.inventory.layout.LayoutRepository;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class InventoryApplier {

    public static void apply(Player player) {
        AbstractCustomClass customClass = ClassEnum.getCustomClassByPlayer(player);
        apply(player, customClass);
    }

    public static void apply(Player player, AbstractCustomClass customClass) {
//        clearStorageContents(player);  //自己手动清理

        boolean isTF = ClassU.isTransformationMaster(player);


        InventoryLayout layout = LayoutRepository.resolve(player, customClass);
        layout.asMap().forEach((slot, itemCount) -> {
            int targetSlot = slot;  //适配神秘的幻变大师
            ItemKind itemKind = itemCount.getKind();
            int count = itemCount.getCount();
            ItemStack itemStack = customClass.createInventoryItem(itemKind, count);
            String itemType = ItemStackU.getStringPersistentData(itemStack, NameSpacedKeys.ITEM_TYPE);
            String itemCategory = ItemStackU.getStringPersistentData(itemStack, NameSpacedKeys.ITEM_CATEGORY);
            if (!AbstractCustomClass.CLASS_SAME.equals(itemCategory)) {
                ItemStackU.removePersistentData(itemStack, NameSpacedKeys.ITEM_TYPE); //新加的 不能让幻变大师影响正常职业物品合并，当然代价就是幻变大师废物消耗品会溢出，石头那些。
            }


            Inventory playerInventory = player.getInventory();
            boolean dontload = false;
            if (isTF && customClass.getClassEnum() != ClassEnum.TRANSFIGURATION_MASTER) {
                //删除血药速度药自带鱿鱼药
                //替换剑稿子弓，删除原本职业特殊物品，添加新职业特殊物品
                if (itemKind == ItemKind.SPEED_POTION || itemKind == ItemKind.HEAL_POTION || itemKind == ItemKind.SQUID_POTION) {
                    dontload = true;  //等价于循环结构的continue
                }

                if (!dontload) {
                    boolean findSameItemType = false;
                    for (int i2 = 0; i2 < playerInventory.getSize(); i2++) {
                        ItemStack playerItemStack = playerInventory.getItem(i2);
                        String playerItemType = ItemStackU.getStringPersistentData(playerItemStack, NameSpacedKeys.ITEM_TYPE);
                        if (playerItemType != null) {
                            if (playerItemType.equals(itemType)) {
                                targetSlot = i2;
                                findSameItemType = true;
                                break;
                            }
                        }
                    }
                    if (!findSameItemType) {
                        ItemStack movedItem = playerInventory.getItem(slot);
                        if (movedItem != null) {
                            playerInventory.setItem(slot, new ItemStack(Material.COMMAND_BLOCK));  //添加一个占位符 强制插入对应位置
                            HashMap<Integer, ItemStack> stack = playerInventory.addItem(movedItem);
                            for (ItemStack extraItemStack : stack.values()) {
                                player.getWorld().dropItemNaturally(player.getLocation(), extraItemStack);
                            }
                        }
                    }

                }


            }
            if (!dontload) {
                player.getInventory().setItem(targetSlot, itemStack);
            }
        });

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
