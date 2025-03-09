package lunaris.core.account;

import lunaris.core.Lunaris;
import lunaris.core.database.AccountRepository;
import lunaris.core.module.IModule;
import lunaris.core.module.ModuleInfo;
import lunaris.core.account.xp.XPManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ModuleInfo(name = "Accounts", dependencies = {"Database"})
public class AccountModule implements IModule {
    private final Lunaris plugin;
    private AccountService accountService;
    private AccountRepository accountRepository;
    private final Map<UUID, Account> accounts = new HashMap<>();
    private XPManager xpManager;

    public AccountModule(Lunaris plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        this.accountRepository = new AccountRepository(plugin);
        this.accountService = new AccountService(plugin, accountRepository);
        this.xpManager = new XPManager(plugin);
    }

    @Override
    public void onDisable() {
        if (accountRepository != null) {
            try {
                accountRepository.close();
            } catch (Exception e) {
                plugin.getLogger().severe("[err->db->account] can't close: " + e.getMessage());
            }
        }
    }

    @Override
    public String getName() {
        return "Accounts";
    }

    @Override
    public ModuleInfo getInfo() {
        return getClass().getAnnotation(ModuleInfo.class);
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public Optional<Account> getAccount(Player player) {
        return accountService.getAccount(player.getUniqueId());
    }

    public XPManager getXPManager() {
        return xpManager;
    }
} 