package lunaris.core.rank;

import lunaris.core.rank.types.IRank;

import net.md_5.bungee.api.ChatColor;

public enum Rank implements IRank {
    OYUNCU(ChatColor.GRAY, "&7Lunarist", 1, "&7Lunarist"),
    VIP(ChatColor.GREEN, "+", 2, "&3++"),
    VIP_PLUS(ChatColor.YELLOW, "++", 3, "&b++"),
    MODERATOR(ChatColor.DARK_AQUA, "Moderatör", 3, "&3Moderatör"),
    GELISTIRICI(ChatColor.DARK_PURPLE, "Geliştirici", 100, "&bGeliştirici"),
    MIMAR(ChatColor.DARK_PURPLE, "Mimar", 100, "&dMimar"),
    YONETICI(ChatColor.RED, "Yönetici", 100, "&cYönetici");

    private final ChatColor color;
    private final String displayName;
    private final int level;
    private final String prefix;

    Rank(ChatColor color, String displayName, int level, String prefix) {
        this.color = color;
        this.displayName = displayName;
        this.level = level;
        this.prefix = prefix;
    }

    @Override
    public ChatColor getColor() {
        return color;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public String getFormattedName() {
        if (this == OYUNCU) {
            return "";
        }
        return color + displayName;
    }

    @Override
    public boolean isAtLeast(IRank rank) {
        return this.level >= rank.getLevel();
    }

    @Override
    public String getPrefix() {
        if (this == OYUNCU) {
            return "";
        }
        return prefix;
    }
} 