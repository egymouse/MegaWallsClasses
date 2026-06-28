package org.ajls.megawallsclasses.inventory.editor;

import org.ajls.megawallsclasses.inventory.key.LayoutKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ItemPreviewProvider {
    ItemStack createPreviewItem(Player player, LayoutKey key);
}
