package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static org.ajls.megawallsclasses.ItemStackModify.setDisplayName;

public class MoleClass extends AbstractCustomClass {
    @Override
    public void initializeClassItems() {
        addClassItem(ItemKind.MOLE_PIE, ClassItemFactory::molePie);
        addClassItem(ItemKind.MOLE_BEEF, ClassItemFactory::moleBeef);



    }

}
