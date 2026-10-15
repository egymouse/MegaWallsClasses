package org.ajls.megawallsclasses.classsystem;

public final class ClassConfigEntry {

    private final String name;
    private final String shortName;
    private final PotionConfig healthPotion;
    private final PotionConfig speedPotion;

    public ClassConfigEntry(
            String name,
            String shortName,
            PotionConfig healthPotion,
            PotionConfig speedPotion
    ) {
        this.name = name;
        this.shortName = shortName;
        this.healthPotion = healthPotion;
        this.speedPotion = speedPotion;
    }

    public String name() {
        return name;
    }

    public String shortName() {
        return shortName;
    }

    public PotionConfig healthPotion() {
        return healthPotion;
    }

    public PotionConfig speedPotion() {
        return speedPotion;
    }

    public static ClassConfigEntry defaultEntry() {
        return new ClassConfigEntry(
                "无职业",
                "无职",
                PotionConfig.healthPotion(2, 1, 16),
                PotionConfig.speedPotion(2, 1, 300, 1)
        );
    }
}
