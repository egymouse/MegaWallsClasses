package org.ajls.megawallsclasses.inventory.layout;

import org.ajls.megawallsclasses.customclasses.AbstractCustomClass;
import org.ajls.megawallsclasses.inventory.key.LayoutKey;

public final class ClassDefaultLayoutResolver {

    //用来给直接创建默认顺序的
    public static InventoryLayout getDefaultLayout(AbstractCustomClass customClass) {
        InventoryLayout layout = DefaultLayouts.common();

        customClass.configureDefaultLayout(layout);

        for (LayoutKey key : customClass.getDefaultLayoutKeys()) {
            if (!layout.contains(key)) {
                int freeSlot = findFirstFreeSlot(layout);
                layout.setSlot(key, freeSlot);
            }
        }

        return layout;
    }

    private static int findFirstFreeSlot(InventoryLayout layout) {
        boolean[] used = new boolean[36];

        for (int slot : layout.asMap().values()) {
            if (slot >= 0 && slot < 36) {
                used[slot] = true;
            }
        }

        for (int i = 0; i < 36; i++) {
            if (!used[i]) {
                return i;
            }
        }

        throw new IllegalStateException("No free inventory slot");
    }
}
