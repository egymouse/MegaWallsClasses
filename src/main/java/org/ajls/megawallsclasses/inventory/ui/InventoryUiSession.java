package org.ajls.megawallsclasses.inventory.ui;

import org.ajls.megawallsclasses.ClassEnum;

public class InventoryUiSession {

    private final UiType type;
    private final ClassEnum targetClass;

    public InventoryUiSession(UiType type) {
        this(type, null);
    }

    public InventoryUiSession(UiType type, ClassEnum targetClass) {
        this.type = type;
        this.targetClass = targetClass;
    }

    public UiType type() {
        return type;
    }

    public ClassEnum targetClass() {
        return targetClass;
    }
}
