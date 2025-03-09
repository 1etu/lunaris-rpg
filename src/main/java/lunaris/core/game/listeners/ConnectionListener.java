package lunaris.core.game.listeners;

import lunaris.core.Lunaris;
import lunaris.core.account.Account;
import lunaris.core.account.AccountModule;
import lunaris.core.account.events.AccountLevelUpdateEvent;
import lunaris.core.account.events.AccountRankUpdateEvent;
import lunaris.core.module.ListenerModule;
import lunaris.core.module.ModuleInfo;
import lunaris.core.utils.PlayerDisplayManager;
import lunaris.rpg.profiles.Profile;
import lunaris.rpg.profiles.ProfileRepository;
import lunaris.rpg.profiles.guis.ProfileManagementGUI;
import lunaris.core.utils.ReasonFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import java.sql.SQLException;
import java.util.List;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ConnectionListener extends ListenerModule {

    public ConnectionListener(Lunaris plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        plugin.getModuleManager().getModule(AccountModule.class).ifPresentOrElse(accountModule -> {
            try {
                Account account = accountModule.getAccountService().getOrCreateAccount(player);
                PlayerDisplayManager.updatePlayerDisplays(player, account);
                
                try {
                    ProfileRepository profileRepository = plugin.getProfileRepository();
                    List<Profile> profiles = profileRepository.findByAccountId(account.getAccountId());
                    
                    if (profiles.isEmpty()) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 2, false, false));
                        
                        plugin.getServer().getScheduler().runTaskLater(plugin, () -> 
                            ProfileManagementGUI.getInventory(plugin).open(player), 5L);
                    } else {
                        PlayerDisplayManager.updatePlayerDisplays(player, account);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    player.kickPlayer(ReasonFactory.PROFILE_ERROR);
                }
            } catch (Exception e) {
                e.printStackTrace();
                player.kickPlayer(ReasonFactory.ACCOUNT_ERROR);
            }
        }, () -> {
            player.kickPlayer(ReasonFactory.ACCOUNT_ERROR);
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Scoreboard scoreboard = player.getServer().getScoreboardManager().getMainScoreboard();
        
        String teamName = "nt_" + player.getName();
        Team team = scoreboard.getTeam(teamName);
        if (team != null) {
            team.unregister();
        }
        
        player.setPlayerListName(null);
    }

    @EventHandler
    public void onRankUpdate(AccountRankUpdateEvent event) {
        Player player = event.getAccount().getPlayer();
        if (player != null && player.isOnline()) {
            Account account = event.getAccount();
            account.setRank(event.getNewRank());
            PlayerDisplayManager.updatePlayerDisplays(player, account);
        }
    }

    @EventHandler
    public void onLevelUpdate(AccountLevelUpdateEvent event) {
        Player player = event.getAccount().getPlayer();
        if (player != null && player.isOnline()) {
            Account account = event.getAccount();
            account.setLevel(event.getNewLevel());
            PlayerDisplayManager.updatePlayerDisplays(player, account);
        }
    }

    @Override
    public String getName() {
        return "ConnectionListener";
    }

    @Override
    public ModuleInfo getInfo() {
        return null;
    }
} 