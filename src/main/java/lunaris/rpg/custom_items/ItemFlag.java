package lunaris.rpg.custom_items;

public enum ItemFlag {
    UNTRADEABLE("Takas Edilemez"),
    UNDROPPABLE("Atılamaz"),
    SOULBOUND("Ruha Bağlı"),
    QUEST_ITEM("Görev Eşyası");

    private final String displayName;

    ItemFlag(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 