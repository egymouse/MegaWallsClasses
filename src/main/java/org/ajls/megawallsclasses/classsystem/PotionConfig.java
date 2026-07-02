package org.ajls.megawallsclasses.classsystem;

public final class PotionConfig {

    private final int amount;
    private final int stackAmount;
    private final int hp;
    private final int durationTicks;
    private final int amplifier;

    private PotionConfig(
            int amount,
            int stackAmount,
            int hp,
            int durationTicks,
            int amplifier
    ) {
        this.amount = amount;
        this.stackAmount = stackAmount;
        this.hp = hp;
        this.durationTicks = durationTicks;
        this.amplifier = amplifier;
    }

    public static PotionConfig healthPotion(int amount, int stackAmount, int hp) {
        return new PotionConfig(amount, stackAmount, hp, 0, 0);
    }

    public static PotionConfig speedPotion(int amount, int stackAmount, int durationTicks, int amplifier) {
        return new PotionConfig(amount, stackAmount, 0, durationTicks, amplifier);
    }

    public int amount() {
        return amount;
    }

    public int stackAmount() {
        return stackAmount;
    }

    public int hp() {
        return hp;
    }

    public int durationTicks() {
        return durationTicks;
    }

    public int amplifier() {
        return amplifier;
    }
}
