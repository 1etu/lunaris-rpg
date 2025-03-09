package lunaris.core.account.xp;

import lunaris.core.Lunaris;
import lunaris.core.account.Account;
import lunaris.core.account.AccountModule;
import lunaris.core.account.events.AccountXPUpdateEvent;
import lunaris.core.utils.PlayerDisplayManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class XPManager {
    private final Lunaris plugin;

    public XPManager(Lunaris plugin) {
        this.plugin = plugin;
    }

    public void addXP(Player player, int amount) {
        plugin.getModuleManager().getModule(AccountModule.class).ifPresent(accountModule -> {
            Account account = accountModule.getAccountService().getOrCreateAccount(player);
            
            AccountXPUpdateEvent event = new AccountXPUpdateEvent(account, account.getXP(), account.getXP() + amount);
            plugin.getServer().getPluginManager().callEvent(event);
            
            if (!event.isCancelled()) {
                int oldLevel = account.getLevel();
                account.addXP(amount);
                
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&3+" + amount + " Kudret &7(" + account.getXP() + "/100)"));

                if (oldLevel != account.getLevel()) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    
                    for (int level = oldLevel + 1; level <= account.getLevel(); level++) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&3Kudret &8" + (level-1) + " &7➵ " + account.getLevelColor() + level));
                    }
                    
                    PlayerDisplayManager.updatePlayerDisplays(player, account);
                }
                
                accountModule.getAccountService().saveAccount(account);
            }
        });
    }
} 