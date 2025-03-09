package lunaris.rpg.custom_items.talismans;

public enum TalismanEffect {
    HEALTH_BOOST("Can Artışı"),
    MANA_REGEN("Mana Yenilenmesi"),
    DAMAGE_BOOST("Hasar Artışı"),
    SPEED_BOOST("Hız Artışı"),
    LUCK("Şans"),
    XP_BOOST("Tecrübe Artışı");

    private final String displayName;

    TalismanEffect(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 