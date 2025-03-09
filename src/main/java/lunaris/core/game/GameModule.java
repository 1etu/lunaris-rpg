package lunaris.core.game;

import lunaris.core.Lunaris;
import lunaris.core.game.listeners.ConnectionListener;
import lunaris.core.game.listeners.ChatListener;
import lunaris.core.module.IModule;
import lunaris.core.module.ListenerModule;
import lunaris.core.module.ModuleInfo;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "Game", dependencies = {"Accounts"})
public class GameModule implements IModule {
    private final Lunaris plugin;
    private final List<ListenerModule> listeners = new ArrayList<>();

    public GameModule(Lunaris plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        listeners.add(new ConnectionListener(plugin));
        listeners.add(new ChatListener(plugin));
        listeners.forEach(ListenerModule::onEnable);
    }

    @Override
    public void onDisable() {
        listeners.forEach(ListenerModule::onDisable);
    }

    @Override
    public String getName() {
        return "Game";
    }

    @Override
    public ModuleInfo getInfo() {
        return getClass().getAnnotation(ModuleInfo.class);
    }
} 