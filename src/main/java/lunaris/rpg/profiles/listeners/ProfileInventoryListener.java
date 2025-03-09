package lunaris.rpg.profiles.listeners;

import lunaris.core.Lunaris;
import lunaris.core.account.AccountModule;
import lunaris.rpg.profiles.ProfileRepository;
import lunaris.rpg.profiles.guis.ProfileManagementGUI;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import java.sql.SQLException;

public class ProfileInventoryListener implements Listener {
    private final Lunaris plugin;
    private final ProfileRepository profileRepository;

    public ProfileInventoryListener(Lunaris plugin) {
        this.plugin = plugin;
        this.profileRepository = new ProfileRepository(plugin);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        
        String title = event.getView().getTitle();
        
        if (title.equals("§8Profil Oluştur") || title.equals("§8Profil Yönetimi")) {
            return;
        }

        try {
            var account = plugin.getModuleManager()
                .getModule(AccountModule.class)
                .map(module -> module.getAccountService().getOrCreateAccount(player))
                .orElseThrow(() -> new IllegalStateException("acc_not_found"));

            var profiles = profileRepository.findByAccountId(account.getAccountId());
            
            if (profiles.isEmpty()) {
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    if (player.isOnline()) {
                        ProfileManagementGUI.getInventory(plugin).open(player);
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "Profil verisi yüklenirken bir hata oluştu!");
        }
    }
} 