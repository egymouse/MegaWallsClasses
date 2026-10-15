package org.ajls.megawallsclasses.customclasses;

import org.ajls.megawallsclasses.InitializeClass;
import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.ajls.megawallsclasses.inventory.key.ItemKind;

public class WardenClass extends AbstractCustomClass {
    @Override
    public void initializeClassItems() {
        addClassItem(ItemKind.WARDEN_SENSOR, () -> {
            return ClassItemFactory.wardenSensor();
        });
    }
}
