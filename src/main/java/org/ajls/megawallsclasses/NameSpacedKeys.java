package org.ajls.megawallsclasses;

import org.bukkit.NamespacedKey;

public class NameSpacedKeys {
    public static NamespacedKey DISPLAY_NAME = new NamespacedKey(MegaWallsClasses.getPlugin(), "DisplayName");  //没用了 以前用displayname作为config的key 现在没用了
    public static NamespacedKey DONT_LOAD = new NamespacedKey(MegaWallsClasses.getPlugin(), "DontLoad");  //没用了 以前通用物品会有两瓶血药 而有些职业只有一瓶 所以要把通用的血药标记 在加载职业时就不加载 当然现在默认血药就是一瓶了所以无所谓，而且也可也通过override来解决。


    public static NamespacedKey ITEM_TYPE = new NamespacedKey(MegaWallsClasses.getPlugin(), "ItemType");  // //存储config的路径,仅限自定义物品栏顺序，给玩家时要去掉 给物品标记类型的 同时用于config路径的标记 当然现在要独立两个功能（路径标记在加载后会取消）所以废弃了。 或者config路径标注和itemType两个用同一个功能？
    //写清晰一点 在没加载时表示config路径，加载后第一用于标记物品类型，第二用于tf来找到对应的物品 当然对于石头木板这些要要删掉防止无法合并。
    //todo 注意判断的时候要阶段后面的index，在LayoutKey里面有对应的方法



    public static NamespacedKey CLASS_ITEM = new NamespacedKey(MegaWallsClasses.getPlugin(), "classItem"); //以后要改成boolean 表示是不是职业物品 如果是就不能被丢出物品栏

//    public static NamespacedKey INVENTORY_PATH = new NamespacedKey(MegaWallsClasses.getPlugin(), "InventoryPath");


    public static NamespacedKey ITEM_CATEGORY = new NamespacedKey(MegaWallsClasses.getPlugin(), "ItemCategory");  //存储tf same 还是diff
    //same表示要更新 diff表示要回收 没有表示别动
    //那怎么表示加载新职业的血药，鱿鱼药那些不要加载，可能还需要一个tf don't load标签来表示不要加载
//
//    public static NamespacedKey CLASS_SAME = new NamespacedKey(MegaWallsClasses.getPlugin(), "ClassSame");  //剑 弓
//    public static NamespacedKey CLASS_DIFF = new NamespacedKey(MegaWallsClasses.getPlugin(), "ClassDiff");  //金胡萝卜 鱿鱼的鱿鱼药

    //tf update  比如剑 弓
    //tf recycle 比如职业特殊物品 如鱿鱼药
}
