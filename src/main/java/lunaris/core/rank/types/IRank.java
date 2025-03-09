package lunaris.core.rank.types;

import net.md_5.bungee.api.ChatColor;

public interface IRank {
    ChatColor getColor();
    String getDisplayName();
    int getLevel();
    String getFormattedName();
    boolean isAtLeast(IRank rank);
    String getPrefix();
} 