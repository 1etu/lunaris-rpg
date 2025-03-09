package lunaris.core.account;

import lunaris.core.Lunaris;
import lunaris.core.account.events.AccountCreateEvent;
import lunaris.core.account.events.AccountLevelUpdateEvent;
import lunaris.core.account.events.AccountRankUpdateEvent;
import lunaris.core.account.services.IAccountService;
import lunaris.core.database.AccountRepository;
import lunaris.core.rank.Rank;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class AccountService implements IAccountService {
    private final Lunaris plugin;
    private final AccountRepository accountRepository;

    public AccountService(Lunaris plugin, AccountRepository accountRepository) {
        this.plugin = plugin;
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getOrCreateAccount(Player player) {
        try {
            Optional<Account> existingAccount = accountRepository.findByUUID(player.getUniqueId());
            if (existingAccount.isPresent()) {
                Account account = existingAccount.get();
                account.updateLastLogin();
                accountRepository.save(account);
                return account;
            }

            Account newAccount = Account.createDefault(player, plugin);
            AccountCreateEvent event = new AccountCreateEvent(newAccount);
            plugin.getServer().getPluginManager().callEvent(event);
            
            if (!event.isCancelled()) {
                accountRepository.save(newAccount);
                return newAccount;
            }
            throw new RuntimeException("ACCOUNT_CREATION_CANCELLED");
        } catch (Exception e) {
            plugin.getErrorManager().handleError("ACC-001", player, e);
            return null;
        }
    }

    @Override
    public Optional<Account> getAccount(UUID uuid) {
        try {
            return accountRepository.findByUUID(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public void updateRank(UUID uuid, Rank rank) {
        getAccount(uuid).ifPresent(account -> {
            AccountRankUpdateEvent event = new AccountRankUpdateEvent(account, account.getRank(), rank);
            plugin.getServer().getPluginManager().callEvent(event);
            
            if (!event.isCancelled()) {
                account.setRank(event.getNewRank());
                try {
                    accountRepository.save(account);
                    Player player = plugin.getServer().getPlayer(uuid);
                    if (player != null && player.isOnline()) {
                        plugin.getModuleManager().getModule(AccountModule.class).ifPresent(module -> {
                            module.getAccountService().getOrCreateAccount(player);  
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void updateLevel(UUID uuid, int level) {
        getAccount(uuid).ifPresent(account -> {
            AccountLevelUpdateEvent event = new AccountLevelUpdateEvent(account, account.getLevel(), level);
            plugin.getServer().getPluginManager().callEvent(event);
            
            if (!event.isCancelled()) {
                account.setLevel(event.getNewLevel());
                try {
                    accountRepository.save(account);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void deleteAccount(UUID uuid) {
        try {
            accountRepository.delete(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveAccount(Account account) {
        try {
            accountRepository.save(account);
        } catch (Exception e) {
            Player player = plugin.getServer().getPlayer(account.getUuid());
            plugin.getErrorManager().handleError("ACC-003", player, e);
        }
    }

    public AccountRepository getAccountRepository() {
        return accountRepository;
    }
} 