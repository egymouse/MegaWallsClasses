package org.ajls.megawallsclasses.customclasses;

import org.ajls.lib.utils.ItemStackU;
import org.ajls.megawallsclasses.*;
import org.ajls.megawallsclasses.classsystem.ClassConfigManager;
import org.ajls.megawallsclasses.commands.Energetic;
import org.ajls.megawallsclasses.inventory.item.ClassItemFactory;
import org.ajls.megawallsclasses.inventory.item.ItemStackProvider;
import org.ajls.megawallsclasses.inventory.key.ItemKind;
import org.ajls.megawallsclasses.inventory.layout.InventoryLayout;
import org.ajls.trackercompass.CompassItemStack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static org.ajls.megawallsclasses.EnergyAccumulate.addEnergy;
import static org.ajls.megawallsclasses.ItemStackModify.*;
import static org.ajls.megawallsclasses.ScoreboardsAndTeams.getScore;

public abstract class AbstractCustomClass {  //emmm其实可以把这个抽象类implement之前那个接口的，但算了反正一样
    private ClassEnum classEnum = null;  // 存储注入的枚举
    public Map<ItemKind, ItemStack> itemKind_itemStack = new HashMap<>();  //不同的职业 同类物品对应不同的itemStack
    public InventoryLayout inventoryLayout = new InventoryLayout();  //直接static，毕竟其他职业以玩家自定义默认物品配置来加载
    public HashMap<ItemKind, Integer> classItemKind_totalStacks = new HashMap<>();

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

    public void addCommonItem(ItemKind itemKind, ItemStackProvider itemStackProvider, int... indexes) {  //如果没indexes就默认加入第一个空位
        //传入的只能是一维数组 要实现指定位置+数量很麻烦
        ItemStack itemStack = itemStackProvider.getItemStack();
        ItemStackU.setStringPersistentData(itemStack, NameSpacedKeys.ITEM_TYPE, itemKind.id());  //注入pdc为了重编辑物品栏时能知道什么在什么位上
        itemKind_itemStack.put(itemKind, itemStack);

        if (this.classEnum == ClassEnum.NONE) {
            if (indexes != null && indexes.length > 0) {
                for (int i = 0; i < indexes.length; i++) {
                    inventoryLayout.setSlot(indexes[i], itemKind, itemStack.getAmount());
                }
            }
            else {
                //say 有bug
                int freeSlot = findFirstFreeSlot(inventoryLayout);
                inventoryLayout.setSlot(freeSlot, itemKind, itemStack.getAmount());
            }

        }


    }

    public static int findFirstFreeSlot(InventoryLayout layout) {
        boolean[] used = new boolean[36];

        for (int slot : layout.asMap().keySet()) {
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

    //itemKind_itemStack每个类都有，来决定对应类的实例物品
    //inventoryLayout是默认的布局，classItemKind_count决定了职业默认物品以及数量（都是默认的）
    //玩家自己会保存，所以有load的都很简单，只需要查询itemKind_itemStack就行了
    //记得补充从config读取和写入的代码，还有默认没用布局时候获取默认布局的代码（读取玩家的通用默认布局，然后加入职业默认物品的itemKind就行了）
    //todo 记得改成slot: itemKind:count的格式
    public void addClassItem(ItemKind itemKind, ItemStackProvider itemStackProvider) {
        addClassItem(itemKind, itemStackProvider, 1);
    }

    public void addClassItem(ItemKind itemKind, ItemStackProvider itemStackProvider, int totalStacks) {  //如果没indexes就默认加入第一个空位
        ItemStack itemStack = itemStackProvider.getItemStack();
// 注意：官方文档指出，服务器目前仅支持英语。若要支持其他语言，需要替换服务端jar内的语言文件。
        ItemStackU.setStringPersistentData(itemStack, NameSpacedKeys.ITEM_TYPE, itemKind.id());  //注入pdc为了重编辑物品栏时能知道什么在什么位上
        itemKind_itemStack.put(itemKind, itemStack);
        classItemKind_totalStacks.put(itemKind, totalStacks);
    }



    public InventoryLayout getDefaultInventoryLayout() {
        return inventoryLayout;
    }

//    List<LayoutKey> getDefaultLayoutKeys() {
//
//    }


    public void initializeCommonItems() {  //todo addcommon只有none会注入默认物品栏配置，其他的只注入itemKind_itemStack，而不写入物品栏配置
        //这是注入itemKind与ItemStack的映射
        // 只有none使用时候是默认配置+物品对应，那其他class使用的时候就需要对应处理了
        // 比方说addItem只是加入一个hashset，然后根据是initialize还是inject layout来决定

        addCommonItem(ItemKind.TOOLKIT, () -> {
            ItemStack item = new ItemStack(Material.CLOCK);
            setDisplayName(item, "超级战墙工具箱");
            setClassItem(item);
            return item;
        }, 0);
        addCommonItem(ItemKind.COMPASS, () -> {
            ItemStack item =  CompassItemStack.trackerCompass.clone();
//            ItemStackModify.setClassItem(item);
            return item;
//            return CompassItemStack.trackerCompass.clone();
        }, 1);
        addCommonItem(ItemKind.PICKAXE, () -> {
            ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
//            setDisplayName(item, "diamond_pickaxe");
            setUnbreakable(item);
            item.addEnchantment(Enchantment.EFFICIENCY, 3);
            setClassItem(item);
            return item;
        }, 2);
        addCommonItem(ItemKind.ENDER_CHEST, () -> {
            ItemStack ender_chest = new ItemStack(Material.ENDER_CHEST);
            setClassItem(ender_chest);
            return ender_chest;
        }, 13);
        addCommonItem(ItemKind.NETHER_STAR, () -> {
            ItemStack nether_star = new ItemStack(Material.NETHER_STAR);
            setClassItem(nether_star);
            return nether_star;
        }, 17);

        addCommonItem(ItemKind.SWORD, () -> {
//            ItemStack iron_sword = new ItemStack(Material.IRON_SWORD);
////            setDisplayName(iron_sword, "iron_sword");
//            setUnbreakable(iron_sword);
//            iron_sword.addEnchantment(Enchantment.UNBREAKING, 3);
//            setClassItem(iron_sword);
//            return iron_sword;

            return createClassSword();
        }, 5);

        addCommonItem(ItemKind.FOOD, () -> {
            ItemStack cooked_porkchop = new ItemStack(Material.COOKED_PORKCHOP, 64);
            return cooked_porkchop;
        }, 6);

        addCommonItem(ItemKind.BOW, () -> {
            return createClassBow();
        }, 7);

        addCommonItem(ItemKind.SPEED_POTION, () -> {
            return createSpeedPotion();
        }, 10);

        addCommonItem(ItemKind.HEAL_POTION, () -> {
            return createHealthPotion();
        }, 8);
        addCommonItem(ItemKind.ARROW, () -> {
            return new ItemStack(Material.ARROW, 64);
        }, 12);
        addCommonItem(ItemKind.COBBLESTONE, () -> {
            return new ItemStack(Material.COBBLESTONE, 64);
        }, 3);
        addCommonItem(ItemKind.PLANKS, () -> {
            return new ItemStack(Material.OAK_PLANKS, 64);
        }, 14);
        addCommonItem(ItemKind.AXE, () -> {
            return new ItemStack(Material.IRON_AXE, 1);
        }, 15);
        addCommonItem(ItemKind.HAY_BLOCK, () -> {
            return new ItemStack(Material.HAY_BLOCK, 16);
        }, 16);
        addCommonItem(ItemKind.SQUID_POTION, () -> {
            return ClassItemFactory.squidPotion();
        }, 4);


        initializeClassItems();
    }

    public void initializeClassItems() {
//        addClassItem(ItemKind.ELAINA_GOLDEN_CARROT, () -> {
//            return new ItemStack(Material.GOLDEN_CARROT, 1);
//        });
    }





    /**
     * 根据 key 生成真实物品。
     * 大部分职业不需要重写全部，只重写 getSwordMaterial / getBow / getPotionProfile 即可。
     */
    public ItemStack createInventoryItem(ItemKind kind, int count) {
        ItemStack itemStack = itemKind_itemStack.get(kind).clone();
        if (count > 0) {
            itemStack.setAmount(count);
        }
        return itemStack;
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
                getHealthPotionAmount()
        );
    }

    public ItemStack createSpeedPotion() {
        return ClassItemFactory.speedPotion(
                getSpeedPotionDurationTicks(),
                getSpeedPotionAmplifier(),
                getSpeedPotionAmount()
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
        if (tryActiveSkillJudge(player, activeSkillType)) {
            tryActiveSkill2(player, activeSkillType);
        }
    }

    public boolean tryActiveSkillJudge(Player player, MyListener.ActiveSkillTypeEnum activeSkillType) {  // //处理左键弓这些是不是activeskill而不是恶灵骑士
        return true;
    }

    public void tryActiveSkill2(Player player, MyListener.ActiveSkillTypeEnum activeSkillType) {   //
        if (Energetic.energeticPlayers.contains(player.getUniqueId())) {
            addEnergy(player,114514);
        }
        if (tryActiveSkill2Judge(player, activeSkillType)) {
            activeSkill(player);
        }
        else {
            player.sendMessage(ChatColor.RED + "差 " + ChatColor.YELLOW + (100 - getScore(player, "energy")) + ChatColor.RED + " 能量");
        }
    }

    public boolean tryActiveSkill2Judge(Player player, MyListener.ActiveSkillTypeEnum activeSkillType) {  //处理能量问题,直接搞一个config处理能量问题也行,特殊职业直接Override，默认就读config的能量上限就好了

        if (getScore(player, "energy") >= 100 ) {
            return true;
        }
        return false;

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
