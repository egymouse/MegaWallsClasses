package org.ajls.megawallsclasses.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ClassKit {
    int classId();

    String name();

    List<ItemKey> itemKeys();

    ItemStack createItem(ItemKey key, Player player);
}
