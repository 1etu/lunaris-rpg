package lunaris.core.account.api;

import lunaris.core.rank.Rank;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.UUID;

public interface IAccount {
    UUID getUuid();
    String getName();
    void setName(String name);
    Rank getRank();
    void setRank(Rank rank);
    int getLevel();
    void setLevel(int level);
    LocalDateTime getFirstLogin();
    LocalDateTime getLastLogin();
    void updateLastLogin();
    Player getPlayer();
    int getXP();
    void setXP(int xp);
    void addXP(int amount);
} 