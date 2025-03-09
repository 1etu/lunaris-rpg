package lunaris.rpg.classes;

import lunaris.core.Lunaris;
import lunaris.core.account.Account;
import lunaris.core.account.AccountModule;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class ClassManager {
    private final Lunaris plugin;

    public ClassManager(Lunaris plugin) {
        this.plugin = plugin;
    }

    public void setClass(Player player, PlayerClass newClass) {
        plugin.getModuleManager().getModule(AccountModule.class).ifPresent(accountModule -> {
            Account account = accountModule.getAccountService().getOrCreateAccount(player);
            PlayerClass oldClass = getClass(player);
            
            if (oldClass == newClass) {
                player.sendMessage(ChatColor.RED + "Uzmanlığın zaten " + newClass.getDisplayName() + "!");
                return;
            }

            account.setPlayerClass(newClass);
            accountModule.getAccountService().saveAccount(account);

            player.sendMessage("");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.WHITE + "Uzmanlığın değiştirildi!");
            player.sendMessage(ChatColor.GRAY + "• " + ChatColor.WHITE + "Eski: " + 
                    (oldClass == PlayerClass.NONE ? ChatColor.GRAY + "Yok" : oldClass.getFormattedName()));
            player.sendMessage(ChatColor.GRAY + "• " + ChatColor.WHITE + "Yeni: " + newClass.getFormattedName());
            player.sendMessage("");
        });
    }

    public void resetClass(Player player) {
        setClass(player, PlayerClass.NONE);
    }

    public boolean hasClass(Player player) {
        return getClass(player) != PlayerClass.NONE;
    }

    public PlayerClass getClass(Player player) {
        try {
            return plugin.getModuleManager().getModule(AccountModule.class)
                    .<PlayerClass>map(accountModule -> {
                        try {
                            String sql = "SELECT player_class FROM accounts WHERE uuid = ?";
                            return accountModule.getAccountService().getAccountRepository()
                                    .findByUUID(player.getUniqueId())
                                    .map(Account::getPlayerClass)
                                    .orElse(PlayerClass.NONE);
                        } catch (SQLException e) {
                            plugin.getLogger().severe("can't get player class bro " + e.getMessage());
                            e.printStackTrace();
                            return PlayerClass.NONE;
                        }
                    })
                    .orElse(PlayerClass.NONE);
        } catch (Exception e) {
            plugin.getLogger().severe("can't get player class bro (second exception): " + e.getMessage());
            e.printStackTrace();
            return PlayerClass.NONE;
        }
    }
} 