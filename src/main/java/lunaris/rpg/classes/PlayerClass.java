package lunaris.rpg.classes;

import org.bukkit.ChatColor;

public enum PlayerClass {
    NONE("None", "Yok", ChatColor.WHITE, ""),
    WARRIOR("Warrior", "Savaşçı", ChatColor.RED, "⚔"),
    MAGE("Mage", "Büyücü", ChatColor.LIGHT_PURPLE, "✦"),
    HEALER("Healer", "Şifacı", ChatColor.GREEN, "❤"),
    ARCHER("Archer", "Okçu", ChatColor.YELLOW, "🏹");

    private final String name;
    private final String displayName;
    private final ChatColor color;
    private final String icon;

    PlayerClass(String name, String displayName, ChatColor color, String icon) {
        this.name = name;
        this.displayName = displayName;
        this.color = color;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getIcon() {
        return icon;
    }

    public String getFormattedName() {
        return color + icon + " " + displayName;
    }

    public static PlayerClass fromString(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NONE;
        }
    }
} 