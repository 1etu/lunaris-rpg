package lunaris.rpg.skills;

import lunaris.core.Lunaris;
import lunaris.core.account.Account;
import lunaris.core.account.AccountModule;
import lunaris.core.module.IModule;
import lunaris.core.module.ModuleInfo;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ModuleInfo(name = "Skills")
public class SkillsManager implements IModule {
    private final Lunaris plugin;
    private final Map<UUID, PlayerSkills> playerSkills;

    public SkillsManager(Lunaris plugin) {
        this.plugin = plugin;
        this.playerSkills = new HashMap<>();
    }

    public PlayerSkills getPlayerSkills(Player player) {
        return playerSkills.computeIfAbsent(player.getUniqueId(), uuid -> {
            Account account = plugin.getModuleManager()
                .getModule(AccountModule.class)
                .map(module -> module.getAccountService().getOrCreateAccount(player))
                .orElseThrow(() -> new IllegalStateException("acc_module_not_found"));
            
            return new PlayerSkills(account);
        });
    }

    public void unloadPlayerSkills(UUID uuid) {
        playerSkills.remove(uuid);
    }

    public void addXp(Player player, SkillType type, int amount) {
        getPlayerSkills(player).addXp(type, amount);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        playerSkills.clear();
    }

    @Override
    public String getName() {
        return "Skills";
    }

    @Override
    public ModuleInfo getInfo() {
        return getClass().getAnnotation(ModuleInfo.class);
    }
} 