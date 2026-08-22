package org.ajls.megawallsclasses.inventory.key;

import java.util.Objects;

public final class LayoutKey {

    private final ItemKind kind;
    private final int index;

    private LayoutKey(ItemKind kind, int index) {
        if (index < 1) {
            throw new IllegalArgumentException("index must >= 1");
        }
        this.kind = kind;
        this.index = index;
    }

    public static LayoutKey of(ItemKind kind) {
        return new LayoutKey(kind, 1);
    }

    public static LayoutKey of(ItemKind kind, int index) {
        return new LayoutKey(kind, index);
    }

    public ItemKind kind() {
        return kind;
    }

    public int index() {
        return index;
    }

    /**
     * 配置文件 / PDC 里保存用。
     *
     * sword
     * bow
     * heal_potion:1
     * heal_potion:2
     * block:1
     * block:2
     */
    public String id() {
        if (index == 1) {
            return kind.id();
        }
        return kind.id() + "_" + index;
    }

//    public static LayoutKey fromId(String id) {
//        String[] parts = id.split("_");
//        ItemKind kind = ItemKind.fromId(parts[0]);
//        int index = parts.length >= 2 ? Integer.parseInt(parts[1]) : 1;
//        return LayoutKey.of(kind, index);
//    }

    public static LayoutKey fromId(String id) {
        int lastUnderscore = id.lastIndexOf('_');
        final ItemKind kind;
        final int index;

        if (lastUnderscore != -1) {
            // 存在下划线：最后一段尝试解析为数字
            String kindPart = id.substring(0, lastUnderscore);
            String indexStr = id.substring(lastUnderscore + 1);
            // 判断末尾是不是数字，防止类似 a_b_c 这种末尾不是数字的情况
            if (isNumeric(indexStr)) {
                kind = ItemKind.fromId(kindPart);
                index = Integer.parseInt(indexStr);
            } else {
                // 末尾不是数字，整体当作kind，index默认1
                kind = ItemKind.fromId(id);
                index = 1;
            }
        } else {
            // 没有下划线
            kind = ItemKind.fromId(id);
            index = 1;
        }
        return LayoutKey.of(kind, index);
    }

    // 简单工具方法：判断字符串是否全为数字
    private static boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) return false;
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LayoutKey)) return false;
        LayoutKey other = (LayoutKey) obj;
        return kind == other.kind && index == other.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, index);
    }

    @Override
    public String toString() {
        return id();
    }
}
