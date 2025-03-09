package lunaris.rpg.custom_items.armors;

public enum ArmorType {
    HELMET("Kask"),
    CHESTPLATE("Göğüslük"),
    LEGGINGS("Pantolon"),
    BOOTS("Botlar");

    private final String displayName;

    ArmorType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 