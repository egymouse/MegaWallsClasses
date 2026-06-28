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

    public static LayoutKey fromId(String id) {
        String[] parts = id.split("_");
        ItemKind kind = ItemKind.fromId(parts[0]);
        int index = parts.length >= 2 ? Integer.parseInt(parts[1]) : 1;
        return LayoutKey.of(kind, index);
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
