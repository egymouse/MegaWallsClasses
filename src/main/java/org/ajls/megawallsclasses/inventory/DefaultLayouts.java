package org.ajls.megawallsclasses.inventory.layout;

import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.ajls.megawallsclasses.inventory.key.LayoutKey;

public final class DefaultLayouts {

    private static final InventoryLayout COMMON = createCommon();

    private DefaultLayouts() {
    }

    private static InventoryLayout createCommon() {
        InventoryLayout layout = new InventoryLayout();

        layout.setSlot(LayoutKey.of(ItemKind.TOOLKIT), 0);
        layout.setSlot(LayoutKey.of(ItemKind.COMPASS), 1);
        layout.setSlot(LayoutKey.of(ItemKind.PICKAXE), 2);

        layout.setSlot(LayoutKey.of(ItemKind.BLOCK, 1), 3);
        layout.setSlot(LayoutKey.of(ItemKind.SQUID_POTION), 4);

        layout.setSlot(LayoutKey.of(ItemKind.SWORD), 5);
        layout.setSlot(LayoutKey.of(ItemKind.FOOD), 6);
        layout.setSlot(LayoutKey.of(ItemKind.BOW), 7);

        layout.setSlot(LayoutKey.of(ItemKind.HEAL_POTION, 1), 8);
        layout.setSlot(LayoutKey.of(ItemKind.HEAL_POTION, 2), 9);

        layout.setSlot(LayoutKey.of(ItemKind.SPEED_POTION, 1), 10);
        layout.setSlot(LayoutKey.of(ItemKind.SPEED_POTION, 2), 11);

        layout.setSlot(LayoutKey.of(ItemKind.ARROW), 12);
        layout.setSlot(LayoutKey.of(ItemKind.ENDER_CHEST), 13);
        layout.setSlot(LayoutKey.of(ItemKind.BLOCK, 2), 14);
        layout.setSlot(LayoutKey.of(ItemKind.AXE), 15);
        layout.setSlot(LayoutKey.of(ItemKind.HAY_BLOCK), 16);
        layout.setSlot(LayoutKey.of(ItemKind.NETHER_STAR), 17);

        return layout;
    }

    public static InventoryLayout common() {
        return COMMON.copy();
    }
}
