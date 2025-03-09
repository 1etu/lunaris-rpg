package lunaris.core.module;

import lunaris.core.Lunaris;
import org.bukkit.event.Listener;

public abstract class ListenerModule implements IModule, Listener {
    protected final Lunaris plugin;

    public ListenerModule(Lunaris plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void onDisable() {
    }
} 