package lunaris.rpg.custom_items.requirements;

import lunaris.core.Lunaris;
import lunaris.core.account.Account;
import lunaris.core.account.AccountModule;
import lunaris.rpg.custom_items.RPGItem;
import org.bukkit.entity.Player;

public class RequirementChecker {
    private final Lunaris plugin;
    
    public RequirementChecker(Lunaris plugin) {
        this.plugin = plugin;
    }
    
    public boolean meetsRequirements(Player player, RPGItem item) {
        Account account = plugin.getModuleManager()
            .getModule(AccountModule.class)
            .map(module -> module.getAccountService().getOrCreateAccount(player))
            .orElse(null);
            
        if (account == null) return false;

        if (item.getRequiredClass() != null && 
            item.getRequiredClass() != account.getPlayerClass()) {
            return false;
        }

        if (item.getRequiredLevel() > account.getLevel()) {
            return false;
        }

        for (var entry : item.getRequirements().entrySet()) {
            String requirement = entry.getKey();
            int requiredValue = entry.getValue();

            switch (requirement) {
                case "Savaş Seviyesi" -> {
                    if (account.getCombatLevel() < requiredValue) return false;
                }
                case "Güç" -> {
                    return false;
                }
                case "Zeka" -> {
                    if (account.getIntelligence() < requiredValue) return false;
                }
                case "Seviye" -> {
                    if (account.getLevel() < requiredValue) return false;
                }
            }
        }

        return true;
    }
} 