package org.ajls.megawallsclasses.inventory;

public final class DefaultLayouts {

    public static InventoryLayout common() {
        InventoryLayout layout = new InventoryLayout();  //todo 不过上面 layout = common() 要注意复制，实际可以写 common().copy()。

        layout.setSlot(ItemKey.TOOLKIT, 0);
        layout.setSlot(ItemKey.COMPASS, 1);
        layout.setSlot(ItemKey.PICKAXE, 2);
        layout.setSlot(ItemKey.BLOCK_1, 3);
        layout.setSlot(ItemKey.SQUID_POTION, 4);

        layout.setSlot(ItemKey.SWORD, 5);
        layout.setSlot(ItemKey.FOOD, 6);
        layout.setSlot(ItemKey.BOW, 7);
        layout.setSlot(ItemKey.HEAL_POTION_1, 8);
        layout.setSlot(ItemKey.HEAL_POTION_2, 9);

        layout.setSlot(ItemKey.SPEED_POTION_1, 10);
        layout.setSlot(ItemKey.SPEED_POTION_2, 11);
        layout.setSlot(ItemKey.ARROW, 12);
        layout.setSlot(ItemKey.ENDER_CHEST, 13);
        layout.setSlot(ItemKey.BLOCK_2, 14);
        layout.setSlot(ItemKey.AXE, 15);
        layout.setSlot(ItemKey.HAY_BLOCK, 16);
        layout.setSlot(ItemKey.NETHER_STAR, 17);

        return layout;
    }

    public static InventoryLayout snowman() {
        InventoryLayout layout = common();
        layout.setSlot(ItemKey.SNOWMAN_BLOOD_BLOCK, 18);
        return layout;
    }
}
