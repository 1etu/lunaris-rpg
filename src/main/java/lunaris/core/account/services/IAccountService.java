package lunaris.core.account.services;

import lunaris.core.account.Account;
import lunaris.core.rank.Rank;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public interface IAccountService {
    Account getOrCreateAccount(Player player);
    Optional<Account> getAccount(UUID uuid);
    void updateRank(UUID uuid, Rank rank);
    void updateLevel(UUID uuid, int level);
    void deleteAccount(UUID uuid);
} 