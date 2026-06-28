package org.ajls.megawallsclasses.inventory;

import org.ajls.megawallsclasses.inventory.item.CommonItemFactory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SnowmanKit implements ClassKit {

    @Override
    public int classId() {
        return 13;
    }

    @Override
    public String name() {
        return "snowman";
    }

    @Override
    public List<ItemKey> itemKeys() {
        return List.of(
                ItemKey.TOOLKIT,
                ItemKey.COMPASS,
                ItemKey.PICKAXE,
                ItemKey.ENDER_CHEST,
                ItemKey.NETHER_STAR,

                ItemKey.SWORD,
                ItemKey.BOW,
                ItemKey.ARROW,
                ItemKey.FOOD,
                ItemKey.BLOCK_1,
                ItemKey.BLOCK_2,
                ItemKey.AXE,
                ItemKey.HAY_BLOCK,

                ItemKey.SPEED_POTION_1,
                ItemKey.HEAL_POTION_1,
                ItemKey.HEAL_POTION_2,

                ItemKey.SNOWMAN_BLOOD_BLOCK
        );
    }

    @Override
    public ItemStack createItem(ItemKey key, Player player) {
        switch (key) {
            case SWORD:
                return ItemFactories.classSword(Material.DIAMOND_SWORD);

            case BOW:
                return ItemFactories.classBow();

            case HEAL_POTION_1:
                return ItemFactories.healPotion(16, 1, 1);

            case HEAL_POTION_2:
                return ItemFactories.healPotion(16, 1, 2);

            case SPEED_POTION_1:
                return ItemFactories.speedPotion(300, 1, 2, 1);

            case SNOWMAN_BLOOD_BLOCK:
                return ItemFactories.snowmanBloodBlock();

            default:
                return CommonItemFactory.create(key, player);
        }
    }
}
