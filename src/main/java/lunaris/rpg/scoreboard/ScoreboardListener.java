package lunaris.rpg.scoreboard;

import lunaris.core.Lunaris;
import lunaris.rpg.profiles.events.PlayerProfileCreateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardListener implements Listener {
    private final Lunaris plugin;
    private final ScoreboardManager scoreboardManager;
    
    public ScoreboardListener(Lunaris plugin, ScoreboardManager scoreboardManager) {
        this.plugin = plugin;
        this.scoreboardManager = scoreboardManager;
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        scoreboardManager.updateScoreboard(event.getPlayer());
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        fr.minuskube.netherboard.Netherboard.instance().deleteBoard(event.getPlayer());
    }
    
    @EventHandler
    public void onProfileCreate(PlayerProfileCreateEvent event) {
        Player player = event.getPlayer();
        plugin.getModuleManager().getModule(lunaris.core.account.AccountModule.class).ifPresent(accountModule -> {
            lunaris.core.account.Account account = accountModule.getAccountService().getOrCreateAccount(player);
            lunaris.core.utils.PlayerDisplayManager.updatePlayerDisplays(player, account);
        });
        scoreboardManager.updateScoreboard(event.getPlayer());
    }
} 