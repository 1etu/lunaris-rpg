package lunaris.rpg.custom_items;

import net.md_5.bungee.api.ChatColor;

public enum ItemRarity {
    NORMAL("Sıradan", ChatColor.WHITE, "☆"),
    UNIQUE("Kutsal", ChatColor.YELLOW, "★"),
    RARE("Kadim", ChatColor.BLUE, "★★"),
    LEGENDARY("Efsanevi", ChatColor.GOLD, "★★★"),
    FABLED("Destansı", ChatColor.LIGHT_PURPLE, "★★★★"),
    MYTHIC("Tanrısal", ChatColor.DARK_RED, "★★★★★");

    private final String displayName;
    private final ChatColor color;
    private final String stars;

    ItemRarity(String displayName, ChatColor color, String stars) {
        this.displayName = displayName;
        this.color = color;
        this.stars = stars;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getStars() {
        return stars;
    }

    public String format(String text) {
        return color + text;
    }
} 