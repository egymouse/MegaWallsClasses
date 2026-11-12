package org.ajls.megawallsclasses.listeners;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.ItemStackModify;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDropItemListener implements Listener {
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        ItemStack itemStack = item.getItemStack();
        Player player = event.getPlayer();
        GameMode gameMode = player.getGameMode();
        if  (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
            if (ItemStackModify.isClassItem(itemStack)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "按q释放剑弃");
            }
        }
    }
}
