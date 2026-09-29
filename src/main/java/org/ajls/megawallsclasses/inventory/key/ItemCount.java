package org.ajls.megawallsclasses.inventory.key;

import java.util.Objects;

public final class ItemCount {
    private final ItemKind kind;
    private final int count;

    public ItemCount(ItemKind kind, int count) {
//        if (count < 0) {
//            throw new IllegalArgumentException("Count cannot be negative");
//        }
        this.kind = kind;
        this.count = count;
    }

    public ItemKind getKind() { return kind; }
    public int getCount() { return count; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemCount)) return false;
        ItemCount that = (ItemCount) o;
        return count == that.count && Objects.equals(kind, that.kind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, count);
    }

    @Override
    public String toString() {
        return "ItemCount{" + "kind=" + kind + ", count=" + count + '}';
    }
}