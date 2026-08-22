package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.*;
import org.ajls.megawallsclasses.classsystem.ClassConfigManager;
import org.ajls.megawallsclasses.commands.Order;
import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.ajls.megawallsclasses.inventory.item.CommonItemFactory;
import org.ajls.megawallsclasses.inventory.item.ItemStackProvider;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.ajls.megawallsclasses.inventory.key.LayoutKey;
import org.ajls.megawallsclasses.inventory.layout.ClassDefaultLayoutResolver;
import org.ajls.megawallsclasses.inventory.layout.InventoryLayout;
import org.ajls.trackercompass.CompassItemStack;
import org.ajls.trackercompass.TrackerCompass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.ajls.megawallsclasses.ItemStackModify.*;

public abstract class AbstractCustomClass {  //emmm其实可以把这个抽象类implement之前那个接口的，但算了反正一样
    private ClassEnum classEnum = null;  // 存储注入的枚举
    public Map<ItemKind, ItemStack> itemKind_itemStack = new HashMap<>();
    InventoryLayout inventoryLayout = new InventoryLayout();

    AbstractCustomClass() {
    }

    public void setClassEnum(ClassEnum classEnum) {
        this.classEnum = classEnum;
    }

    public ClassEnum getClassEnum() {
        return classEnum;
    }

    // 默认实现：通过枚举获取 classId，再到配置文件读取名称
    public String getClassName() {
        int classId = getClassEnum().classId;
        return ClassConfigManager.getInstance(MegaWallsClasses.getPlugin())
                .getClassName(classId);
    }

    public String getClassShortName() {
        int classId = getClassEnum().classId;
        return ClassConfigManager.getInstance(MegaWallsClasses.getPlugin())
                .getClassShortName(classId);
    }

    public void addItem(ItemKind itemKind, ItemStackProvider itemStackProvider, int... indexes) {  //如果没indexes就默认加入第一个空位
        ItemStack itemStack = itemStackProvider.getItemStack();
        ItemStackU.setStringPersistentData(itemStack, NameSpacedKeys.ITEM_TYPE, itemKind.id());  //注入pdc为了重编辑物品栏时能知道什么在什么位上
        itemKind_itemStack.put(itemKind, itemStack);
        if (indexes != null && indexes.length > 0) {
            for (int i = 0; i < indexes.length; i++) {
                LayoutKey layoutKey = LayoutKey.of(itemKind, i+1);
                inventoryLayout.setSlot(layoutKey, indexes[i]);
            }
        }
        else {
            //say 有bug
            int freeSlot = ClassDefaultLayoutResolver.findFirstFreeSlot(inventoryLayout);
            inventoryLayout.setSlot(LayoutKey.of(itemKind), freeSlot);
        }


    }


    public InventoryLayout getDefaultInventoryLayout() {
        return inventoryLayout;
    }

//    List<LayoutKey> getDefaultLayoutKeys() {
//
//    }


    public void createItemStackForItemKind() {  //这是注入itemKind与ItemStack的映射

        addItem(ItemKind.TOOLKIT, () -> {
            ItemStack item = new ItemStack(Material.CLOCK);
            setDisplayName(item, "超级战墙工具箱");
            setClassItem(item);
            return item;
        }, 0);
        addItem(ItemKind.COMPASS, () -> {
            return CompassItemStack.trackerCompass.clone();
        }, 1);
        addItem(ItemKind.PICKAXE, () -> {
            ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
//            setDisplayName(item, "diamond_pickaxe");
            setUnbreakable(item);
            item.addEnchantment(Enchantment.EFFICIENCY, 3);
            setClassItem(item);
            return item;
        }, 2);
        addItem(ItemKind.ENDER_CHEST, () -> {
            ItemStack ender_chest = new ItemStack(Material.ENDER_CHEST);
            setClassItem(ender_chest);
            return ender_chest;
        }, 13);
        addItem(ItemKind.NETHER_STAR, () -> {
            ItemStack nether_star = new ItemStack(Material.NETHER_STAR);
            setClassItem(nether_star);
            return nether_star;
        }, 17);

        addItem(ItemKind.SWORD, () -> {
//            ItemStack iron_sword = new ItemStack(Material.IRON_SWORD);
////            setDisplayName(iron_sword, "iron_sword");
//            setUnbreakable(iron_sword);
//            iron_sword.addEnchantment(Enchantment.UNBREAKING, 3);
//            setClassItem(iron_sword);
//            return iron_sword;

            return createClassSword();
        }, 5);

        addItem(ItemKind.FOOD, () -> {
            ItemStack cooked_porkchop = new ItemStack(Material.COOKED_PORKCHOP, 64);
            return cooked_porkchop;
        }, 6);

        addItem(ItemKind.BOW, () -> {
            return createClassBow();
        }, 7);

        addItem(ItemKind.SPEED_POTION, () -> {
            return createSpeedPotion();
        }, 10);

        addItem(ItemKind.HEAL_POTION, () -> {
            return createHealthPotion();
        }, 8);
        addItem(ItemKind.ARROW, () -> {
            return new ItemStack(Material.ARROW, 64);
        }, 12);
        addItem(ItemKind.COBBLESTONE, () -> {
            return new ItemStack(Material.COBBLESTONE, 64);
        }, 3);
        addItem(ItemKind.PLANKS, () -> {
            return new ItemStack(Material.OAK_PLANKS, 64);
        }, 14);
        addItem(ItemKind.AXE, () -> {
            return new ItemStack(Material.IRON_AXE, 64);
        }, 15);
        addItem(ItemKind.HAY_BLOCK, () -> {
            return new ItemStack(Material.HAY_BLOCK, 16);
        }, 16);
        addItem(ItemKind.SQUID_POTION, () -> {
            return ClassItemFactory.squidPotion();
        }, 4);



    }

    public void createClassItemStackForItemKind() {
        //给其他class 比如摩尔来override用的
    }

    public List<LayoutKey> getClassDefaultLayoutKeys() {
        return new ArrayList<>();  //给其他class override用的
    }

    public void configureDefaultLayout(InventoryLayout layout) {
        // 默认不做任何事
    }


    /**
     * 根据 key 生成真实物品。
     * 大部分职业不需要重写全部，只重写 getSwordMaterial / getBow / getPotionProfile 即可。
     */
    public ItemStack createInventoryItem(LayoutKey key) {
        return itemKind_itemStack.get(key.kind());
    }

    public static final String CLASS_SAME = "ClassSame";  //比如剑 弓
    public static final String CLASS_DIFF = "ClassDiff";  //比如魔女药 鱿鱼的鱿鱼药
//    public static final String CLASS_NOT_LOAD = "ClassNotLoad";  //比如速度药血药 通用鱿鱼药  不对啊 只要遍历新物品 筛选class Same和class diff加载 当然这之前原物品栏要遍历删除class diff物品


    public ItemStack createClassSword() {
//        ItemStack classSword = new ItemStack(Material.IRON_SWORD);
//        setUnbreakable(classSword);
//        setClassItem(classSword);
//        addLore(classSword, "classSword");
//        classSword.addEnchantment(Enchantment.UNBREAKING, 3);
//        ItemStackU.setStringPersistentData(classSword, NameSpacedKeys.ITEM_CATEGORY, CLASS_SAME);
//        return classSword;


        return ClassItemFactory.classSword(Material.IRON_SWORD);

        //可以用ClassItemFactory的模板来快速加载职业武器
    }

    public ItemStack createClassBow() {
        return ClassItemFactory.classBow();

    }


    public ItemStack createHealthPotion() {
        return ClassItemFactory.healPotion(
                getHealthPotionHp(),
                getHealthPotionStackAmount()
        );
    }

    public ItemStack createSpeedPotion() {
        return ClassItemFactory.speedPotion(
                getSpeedPotionDurationTicks(),
                getSpeedPotionAmplifier(),
                getSpeedPotionStackAmount()
        );
    }




    public int getHealthPotionHp() {
        return getClassConfigManager().getHealthPotionHp(getClassEnum().classId);
    }

    public int getHealthPotionAmount() {
        return getClassConfigManager().getHealthPotionAmount(getClassEnum().classId);
    }

    public int getHealthPotionStackAmount() {
        return getClassConfigManager().getHealthPotionStackAmount(getClassEnum().classId);
    }

    public int getSpeedPotionAmount() {
        return getClassConfigManager().getSpeedPotionAmount(getClassEnum().classId);
    }

    public int getSpeedPotionDurationTicks() {
        return getClassConfigManager().getSpeedPotionDurationTicks(getClassEnum().classId);
    }

    public int getSpeedPotionAmplifier() {
        return getClassConfigManager().getSpeedPotionAmplifier(getClassEnum().classId);
    }

    public int getSpeedPotionStackAmount() {
        return getClassConfigManager().getSpeedPotionStackAmount(getClassEnum().classId);
    }

    protected ClassConfigManager getClassConfigManager() {
        return ClassConfigManager.getInstance(MegaWallsClasses.getPlugin());
    }



//    public boolean hasInventoryItem(LayoutKey key) {
//        return getDefaultLayoutKeys().contains(key);
//    }

    public int getPotionCount(ItemKind kind) {
        if (kind == ItemKind.HEAL_POTION) return getHealthPotionAmount();
        if (kind == ItemKind.SPEED_POTION) return getSpeedPotionAmount();
        return 1;
    }





//    default String getClassName() {
//        return "没有职业";
//    }
//
//    default String getClassSimpleName() {
//        return "无职";
//    }


    public void test() {

    }

    // ---------------- 抽离：职业专属提示（通用默认实现）----------------
    public void sendSkillReadyMessage(Player player) {
        // 原代码：非3职业的通用提示
        player.sendMessage(ChatColor.GREEN + "终极技能 " + ChatColor.YELLOW + "准备就绪");
    }

    // ---------------- 抽离：职业专属效果（通用默认实现：无效果）----------------
    public void applySkillReadyEffect(Player player) {
        // 普通职业：没有额外效果
        //末影人
    }

//    default void onTridentDamage(Player player) {
//        //老版本溺尸王
//        //todo 写好老版本溺尸王 三叉戟靠忠诚，但是保留命令方块设计（或者加一个不能放置的屏障） 如果背包满也会自动进入屏障 然后触发主动6秒内打中人才行（非扔出），或者不触发满能量打中人也行
//    }

    public void tryActiveSkill(Player player, MyListener.ActiveSkillTypeEnum activeSkillType) {

    }

    public void activeSkill(Player player) {

    }

    // ===================== 新增：三叉戟发射事件方法 =====================
    // 默认空实现：其他职业完全不用管，只有溺尸王重写
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        // 普通职业：无任何逻辑
    }

    public void onProjectileHit(ProjectileHitEvent event, Player player) {}

//    default void onTridentDamage(EntityDamageByEntityEvent event) {
//
//    }

    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

    }


    public void onPlayerPickupArrow(PlayerPickupArrowEvent event) {

    }

    public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {

    }

    public void onPlayerMove(PlayerMoveEvent event) {

    }

    public void onEntityShootBow(EntityShootBowEvent event) {

    }


}
