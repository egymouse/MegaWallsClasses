package org.ajls.megawallsclasses.inventory.key;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum ItemKind {
        /*todo 血药，速度药改成一个就行，最好备战未来适配多个重复物品（比如说两组血药）
    fromid改成hashmap存储
    default layouts的layout如何复制？最好用static，在加载时执行一次就好。
    职业的default layouts可以通过寻找可用最小索引添加
    classKit的话 目前我是有自己的职业管理系统 看能不能集成 包括registry
    职业剑，弓，速度药，血药需要默认值 比如大部分职业血药都是两瓶16hp
    config有必要改成markdirty然后只在服务器启动/停止或者每隔几分钟保存config吗
    Cannot resolve method 'or' in 'OptionalInt' 在laayout resolver的resolve
    InventoryOrderEditor那块没怎么听懂，附上MyListener原本的实现，同时给出自己玩家打开物品栏用state machine管理的构想，就不用title来管理了



    最后包结构我也不会，看看文件放在哪个包下好
     */

    /*
    LayoutKey并没copy，会不会有什么问题
    这default layout的common 只有ClassDefaultLayoutResolver getDefaultLayout才修改，其他都是只读，copy会不会浪费性能
    如果class想要自定义职业默认layout特殊物品的位置呢？ 估计不会
    getDefaultLayoutKeys能不能在override执行增量操作 可以call super就行

    血药，速度药这些信息配置在config里面
    需要config结构重置 包括之前的职业名称

    把itemFactory在生成key的时候就绑定在一起，减少物品反复生成和if判断

    class sword和class bow是先获取白板，然后再添加新东西

    getHealthPotionHp(int index)这些index是何意味
    速度药水是300tick
    loadHealthPotionConfig和loadSpeedPotionConfig没对应method(ClassConfigManager)
    为什么createClassSword(Player player, LayoutKey key）需要加入这么多参数的？pdc又是什么？
    重排物品栏顺序入口不是order命令，而是右键种打开的MW menu菜单，可以选择重新排序物品栏还是职业物品栏，计划搞一个职业类型选择菜单，也就是选择自己做的职业还是饭制职业还是d服职业之类的，当然职业物品栏顺序重排也会有这个菜单。
    根据代码生成一下所有职业的职业特殊物品，包括实际的和preview的,看看能不能在职业物品栏重排时就展示职业物品（比如如果职业是钻石剑什么的就要展示对应的钻石剑而非铁剑）
    速度药是默认一组叠起来的两个，血药也是。
    对物品的mark其实只是为了适配幻变大师特别的机制，有三类型物品，第一类是剑/弓这些可以直接替换的物品，第二类就是消耗品不给予补充(专指速度药，血药，还有给每个人的鱿鱼药，如果有职业，第三类就是职业特殊物品，我想清除掉之前的然后补发下一个职业的。isTF表示是不是幻变大师，loadAsTF表示是不是初始化职业（也就是要加载幻变大师的补给品那些）
    squid_potion_for_everyone本质上和squid_potion没有区别，只是每个职业都有所以取了一个特殊名字，唯一区别就是是要适配tf消耗品不给补充的机制，给大家的鱿鱼药不补充，但是如果是作为职业专属物品（我后面计划给鱿鱼多补5组药，每组默认三个，等价于是一共有6组），比如鱿鱼，那么那专属的5组药会补充。









     */
    TOOLKIT("超级战墙工具箱"),
    COMPASS("compass"),
    PICKAXE("diamond_pickaxe"),
    ENDER_CHEST("ender_chest"),
    NETHER_STAR("nether_star"),

    SWORD("iron_sword"),
    BOW("bow"),
    ARROW("arrow"),

    BLOCK("block"),
    COBBLESTONE("cobblestone"),
    PLANKS("plank"),
    FOOD("cooked_porkchop"),
    AXE("iron_axe"),
    HAY_BLOCK("hay_block"),

    SPEED_POTION("speed_potion"),
    HEAL_POTION("heal_potion"),

    SQUID_POTION("squid_potion_everyone"),

    // 职业特殊物品
    SNOWMAN_SHOVEL("snowman_shovel"),
    SNOWMAN_SNOW_BLOCK,
    SNOWMAN_PUMPKIN,
    MOLE_PIE("mole_pie"),
    MOLE_BEEF("mole_beef"),
    ELAINA_GOLDEN_CARROT("elaina_golden_carrot"),
//    ELAINA_IRON_SWORD("elaina_iron_sword"),
    ELAINA_POTION("elaina_potion"),
    WARDEN_SENSOR("warden_sensor");
//    DRAGON_SWORD;

    private final String id;

    ItemKind(String id) {
        this.id = id;
    }

    ItemKind() {
        this.id = this.name().toLowerCase(Locale.ENGLISH);
    }

    public String id() {
        return id;
    }

    private static final Map<String, ItemKind> BY_ID = new HashMap<>();

    static {
        for (ItemKind kind : values()) {
            BY_ID.put(kind.id, kind);
        }
    }

    public static ItemKind fromId(String id) {
        ItemKind kind = BY_ID.get(id);
        if (kind == null) {
            throw new IllegalArgumentException("Unknown item kind: " + id);
        }
        return kind;
    }

    @Override
    public String toString() {
        return this.id;
    }
}
