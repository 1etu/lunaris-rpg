package lunaris.core;

import lunaris.core.account.AccountModule;
import lunaris.core.database.DatabaseModule;
import lunaris.core.game.GameModule;
import lunaris.core.module.ModuleManager;
import lunaris.core.command.CommandManager;
import lunaris.core.command.commands.HelpCommand;
import lunaris.core.command.commands.CommandManagerCommand;
import lunaris.core.command.commands.account.AccountCommand;
import lunaris.core.rank.RankManager;
import lunaris.rpg.classes.ClassManager;
import lunaris.rpg.custom_items.commands.ItemCommand;
import lunaris.rpg.custom_items.registry.ItemRegistry;
import lunaris.rpg.profiles.listeners.ProfileInventoryListener;
import fr.minuskube.inv.InventoryManager;
import lunaris.rpg.profiles.ProfileRepository;
import lunaris.core.utils.ReasonFactory;
import lunaris.rpg.scoreboard.ScoreboardManager;
import lunaris.rpg.scoreboard.ScoreboardListener;
import lunaris.core.command.commands.debug.DebugCommand;
import lunaris.rpg.npc.NPCManager;
import lunaris.core.error.ErrorManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Lunaris extends JavaPlugin {
    
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private RankManager rankManager;
    private ClassManager classManager;
    private InventoryManager invManager;
    private ProfileRepository profileRepository;
    private NPCManager npcManager;
    private ErrorManager errorManager;

    // Comment<1etuuu>: Don't try to change hierarchy, it'll break everything :D
    @Override
    public void onEnable() {
        this.errorManager = new ErrorManager(this);
        this.moduleManager = new ModuleManager(this);
        this.npcManager = new NPCManager(this);

        this.invManager = new InventoryManager(this);
        invManager.init();

        moduleManager.registerModule(new DatabaseModule(this));
        moduleManager.registerModule(new AccountModule(this));
        moduleManager.registerModule(new GameModule(this));
        
        moduleManager.enableModules();
        
        this.commandManager = new CommandManager(this);
        this.rankManager = new RankManager(this);
        this.classManager = new ClassManager(this);
        this.profileRepository = new ProfileRepository(this);
        
        commandManager.registerCommand(new HelpCommand(this));
        commandManager.registerCommand(new CommandManagerCommand(this));
        commandManager.registerCommand(new AccountCommand(this));
        commandManager.registerCommand(new DebugCommand(this));
        commandManager.registerCommand(new ItemCommand(this));

        getServer().getPluginManager().registerEvents(new ProfileInventoryListener(this), this);

        ScoreboardManager scoreboardManager = new ScoreboardManager(this);
        getServer().getPluginManager().registerEvents(
            new ScoreboardListener(this, scoreboardManager), 
            this
        );
        
        getServer().getScheduler().runTask(this, () -> {
            moduleManager.registerModule(new lunaris.rpg.quests.QuestManager(this));
            moduleManager.enableModule(moduleManager.getModule("Quests").orElseThrow());
        });

        ItemRegistry.initialize();
        
        getLogger().info("OK");
    }

    @Override
    public void onDisable() {
        getServer().getOnlinePlayers().forEach(player -> 
            player.kickPlayer(ReasonFactory.SERVER_RESTART)
        );

        if (moduleManager != null) {
            moduleManager.disableModules();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        npcManager.getPresetManager().spawnNPCsForPlayer(event.getPlayer());
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    public ClassManager getClassManager() {
        return classManager;
    }

    public InventoryManager getInventoryManager() {
        return invManager;
    }

    public ProfileRepository getProfileRepository() {
        return profileRepository;
    }

    public NPCManager getNPCManager() {
        return npcManager;
    }

    public ErrorManager getErrorManager() {
        return errorManager;
    }
}