package org.ajls.megawallsclasses.inventory.item;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.*;
import org.ajls.megawallsclasses.classsystem.ClassConfigKeys;
import org.ajls.megawallsclasses.customclasses.AbstractCustomClass;
import org.ajls.megawallsclasses.customclasses.WardenClass;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.ajls.megawallsclasses.inventory.key.LayoutKey;
import org.ajls.megawallsclasses.utils.PotionU;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import static org.ajls.megawallsclasses.ItemStackModify.*;
import static org.ajls.megawallsclasses.MegaWallsClasses.setEffect;

public final class ClassItemFactory {

    private ClassItemFactory() {
    }

    public static ItemStack classSword(Material material) {
        return classSword(material, true);
    }

    public static ItemStack classSword(Material material, boolean unbreaking) {
        ItemStack item = new ItemStack(material);
        setUnbreakable(item);
        setClassItem(item);
        if (unbreaking) {
            item.addEnchantment(Enchantment.UNBREAKING, 3);
        }
        ItemStackU.setStringPersistentData(item, NameSpacedKeys.ITEM_CATEGORY, AbstractCustomClass.CLASS_SAME);
        return item;
        //物品不用mark，职业在配置enum与物品对应时才mark，
    }


    public static ItemStack classBow() {
        ItemStack item = new ItemStack(Material.BOW);
        setUnbreakable(item);
        setClassItem(item);
        item.addEnchantment(Enchantment.UNBREAKING, 3);
        ItemStackU.setStringPersistentData(item, NameSpacedKeys.ITEM_CATEGORY, AbstractCustomClass.CLASS_SAME);

        return item;

    }



    public static ItemStack healPotion(int hp, int amount) {
        ItemStack item = new ItemStack(Material.POTION, amount);

        setBasePotionTye(item, PotionType.HEALING);
        setEffect(item, PotionEffectType.INSTANT_HEALTH, hp, 0);

        setDisplayName(
                item,
                ChatColor.RED + "" + hp + "HP"
        );

        addLore(item, ClassConfigKeys.HEALTH_POTION);

        setMaxStackSize(item, amount);
        setClassItem(item);

        return item;
    }

    public static ItemStack speedPotion(int durationTicks, int amplifier, int amount) {
        ItemStack item = new ItemStack(Material.POTION, amount);

        setBasePotionTye(item, PotionType.SWIFTNESS);
        setEffect(item, PotionEffectType.SPEED, durationTicks, amplifier, false, true);

        setDisplayName(
                item,
                ChatColor.AQUA + "" + durationTicks / 20 + "s " + toRomanPotionLevel(amplifier)
        );

        addLore(item, ClassConfigKeys.CUSTOM_POTION);
        addLore(item, ClassConfigKeys.SPEED_POTION);

        setMaxStackSize(item, amount);
        setClassItem(item);

        return item;
    }

    public static ItemStack squidPotion() {
        ItemStack squid_potion = new ItemStack(Material.POTION, 3);
        ItemStackModify.setBasePotionTye(squid_potion, PotionType.NIGHT_VISION);
//        PotionU.setColor(squid_potion, 255, 255, 255);
        PotionU.setColor(squid_potion, Color.BLACK);
        ItemStackModify.setEffect(squid_potion, PotionEffectType.ABSORPTION, 1200, 1);
//        setDisplayName(squid_potion, ChatColor.GOLD + "\"陈皮茶\"");  //60s IIv  //60s 8HP(II)
        setDisplayName(squid_potion, ChatColor.GOLD + "60s 8HP(II)");  //60s IIv  //60s 8HP(II)
//        addLore(squid_potion, "感觉怪怪的...(限定款)");  //ChatColor.GOLD +  //跟陈皮茶的味道差不多(限定款)
        addLore(squid_potion, "squid_potion");
        addLore(squid_potion, "custom_potion");

//        ItemStackU.setStringPersistentData(squid_potion, NameSpacedKeys.ITEM_CATEGORY, AbstractCustomClass.CLASS_DIFF);
        ItemStackModify.setMaxStackSize(squid_potion, 3);
        return squid_potion;
    }

    public static ItemStack snowmanBloodBlock() {
        ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);
        setDisplayName(item, "snowman_blood_block");
        setClassItem(item);

        return mark(LayoutKey.of(ItemKind.SNOWMAN_SNOW_BLOCK), item);
    }

    public static ItemStack molePie() {
        ItemStack item = new ItemStack(Material.PUMPKIN_PIE, 64);
        setDisplayName(item, "mole_pie");
        addLore(item, "junk_food");
//        setClassItem(item);
        ItemStackU.setStringPersistentData(item, NameSpacedKeys.ITEM_CATEGORY, AbstractCustomClass.CLASS_DIFF);
        return mark(LayoutKey.of(ItemKind.MOLE_PIE), item);
    }

    public static ItemStack moleBeef() {
        ItemStack item = new ItemStack(Material.BEEF, 8);
        setDisplayName(item, "mole_beef");
        addLore(item, "junk_food");
//        setClassItem(item);
        ItemStackU.setStringPersistentData(item, NameSpacedKeys.ITEM_CATEGORY, AbstractCustomClass.CLASS_DIFF);
        return mark(LayoutKey.of(ItemKind.MOLE_BEEF), item);
    }

    public static ItemStack wardenSensor() {
        ItemStack item = new ItemStack(Material.SCULK_SENSOR, WardenClass.WARDEN_MAX_SENSOR_COUNT);
//        setDisplayName(item, "warden_sensor");
//        setClassItem(item);
        ItemStackU.setStringPersistentData(item, NameSpacedKeys.ITEM_CATEGORY, AbstractCustomClass.CLASS_DIFF);
        return mark(LayoutKey.of(ItemKind.WARDEN_SENSOR), item);
    }

    public static ItemStack caveSpiderShovel() {
        ItemStack item = new ItemStack(Material.IRON_SHOVEL, 1);
        ItemStackU.setStringPersistentData(item, NameSpacedKeys.ITEM_CATEGORY, AbstractCustomClass.CLASS_DIFF);
        return mark(LayoutKey.of(ItemKind.CAVE_SPIDER_SHOVEL), item);
    }

    public static ItemStack ghostRiderPumpkin() {
        ItemStack helmet = InitializeClass.getClassItem(Material.JACK_O_LANTERN, false);
        helmet.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
        return helmet;
    }

    public static ItemStack ghostRiderLantern() {
        ItemStack helmet = InitializeClass.getClassItem(Material.JACK_O_LANTERN, false);
        helmet.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
        return helmet;
    }


    public static ItemStack mark(LayoutKey key, ItemStack item) {
//        ItemStackU.setStringPersistentData(item, NameSpacedKeys.ITEM_TYPE, key.id());
        return item;  //在addItem里面自动mark了
    }

    private static String toRomanPotionLevel(int amplifier) {
        int level = amplifier + 1;

        switch (level) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            default:
                return String.valueOf(level);
        }
    }
}
