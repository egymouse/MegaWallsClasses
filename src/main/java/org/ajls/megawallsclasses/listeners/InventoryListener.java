package org.ajls.megawallsclasses.listeners;

import org.ajls.megawallsclasses.MyListener;
import org.ajls.megawallsclasses.inventory.editor.InventoryOrderEditor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import static org.ajls.megawallsclasses.InventoryManager.createWhichClassReorderInventory;
import static org.ajls.megawallsclasses.MegaWallsClasses.plugin;

public class InventoryListener implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        InventoryView inventoryView = event.getView();
        Inventory topInventory = inventoryView.getTopInventory();
        Inventory bottomInventory = inventoryView.getBottomInventory();
        String title = event.getView().getTitle();

        Player player = (Player) event.getPlayer();
        if (player.hasMetadata("ReorderInventory")) {
            player.removeMetadata("ReorderInventory", plugin);
        }

        if (title.equals("Reorder inventory")) {  //todo 记得改成不能偷物品，甚至不能drag以及丢弃，甚至放在鼠标上然后关闭物品栏让物品自然掉出，可以用custom event（自己写一个event接入bukkit api)来实现多事件的整合
            InventoryOrderEditor.saveCommonEditor(player, topInventory);
            player.sendMessage(ChatColor.GREEN + "物品栏成功保存");
        }
        else if (title.equals("ClassReorderInventory")) {
            int index = MyListener.player_whichClassReorder.get(player.getUniqueId());
            InventoryOrderEditor.saveClassEditor(player, topInventory, index);
            player.openInventory(createWhichClassReorderInventory(player));
            player.sendMessage(ChatColor.GREEN + "职业物品栏成功保存");
        }
    }
}
