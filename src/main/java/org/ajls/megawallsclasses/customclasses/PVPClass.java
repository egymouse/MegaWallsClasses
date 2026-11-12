package org.ajls.megawallsclasses.customclasses;

import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.ajls.megawallsclasses.inventory.key.ItemKind;

public class PVPClass extends AbstractCustomClass{


    @Override
    public void initializeClassItems() {
        addClassItem(ItemKind.MOLE_PIE, ClassItemFactory::molePie);
        addClassItem(ItemKind.MOLE_BEEF, ClassItemFactory::moleBeef);
    }
}
