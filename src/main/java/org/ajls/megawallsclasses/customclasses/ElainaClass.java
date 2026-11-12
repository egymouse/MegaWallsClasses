package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.InitializeClass;
import org.ajls.megawallsclasses.NameSpacedKeys;
import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import static org.ajls.megawallsclasses.ItemStackModify.setAttributeAttackDamage;
import static org.ajls.megawallsclasses.ItemStackModify.setDisplayName;

public class ElainaClass extends AbstractCustomClass {
    @Override
    public ItemStack createClassSword() {
        ItemStack classSword = ClassItemFactory.classSword(Material.STICK, false); //you can't add unbreaking on a stick
        classSword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        classSword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
        setAttributeAttackDamage(classSword, 5); //original 4
        return classSword;
    }

    @Override
    public ItemStack createClassBow() {
        ItemStack classBow = ClassItemFactory.classBow();
        classBow.addEnchantment(Enchantment.POWER, 2);
        classBow.addEnchantment(Enchantment.INFINITY, 1);
        return classBow;
    }

    @Override
    public void initializeClassItems() {
        addClassItem(ItemKind.ELAINA_GOLDEN_CARROT, () -> {
            ItemStack itemStack = new ItemStack(Material.GOLDEN_CARROT, 5);
            return ItemStackU.setStringPersistentData(itemStack, NameSpacedKeys.ITEM_CATEGORY, AbstractCustomClass.CLASS_DIFF);
        });
        addClassItem(ItemKind.ELAINA_POTION, () -> {
            return InitializeClass.elaina_potion();
        });



    }

}
