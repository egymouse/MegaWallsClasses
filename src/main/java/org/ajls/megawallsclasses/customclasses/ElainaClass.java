package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.InitializeClass;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static org.ajls.megawallsclasses.ItemStackModify.setDisplayName;

public class ElainaClass extends AbstractCustomClass {

    @Override
    public void initializeClassItems() {
        addClassItem(ItemKind.ELAINA_GOLDEN_CARROT, () -> {
            return new ItemStack(Material.GOLDEN_CARROT, 5);
        });
        addClassItem(ItemKind.ELAINA_POTION, () -> {
            return InitializeClass.elaina_potion();
        });



    }

}
