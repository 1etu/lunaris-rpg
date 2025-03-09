package lunaris.core.command;

import lunaris.core.Lunaris;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final Lunaris plugin;
    private final List<BaseCommand> commands;
    private CommandMap commandMap;

    public CommandManager(Lunaris plugin) {
        this.plugin = plugin;
        this.commands = new ArrayList<>();
        setupCommandMap();
    }

    private void setupCommandMap() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerCommand(BaseCommand command) {
        CommandInfo info = command.getClass().getAnnotation(CommandInfo.class);
        if (info == null) {
            throw new IllegalArgumentException("where is @CommandInfo :D?");
        }

        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            
            PluginCommand pluginCommand = constructor.newInstance(info.name(), plugin);
            pluginCommand.setDescription(info.description());
            pluginCommand.setUsage(info.usage());
            pluginCommand.setAliases(List.of(info.aliases()));
            pluginCommand.setExecutor(command);
            pluginCommand.setTabCompleter(command);

            commandMap.register(plugin.getName(), pluginCommand);
            commands.add(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<BaseCommand> getCommands() {
        return new ArrayList<>(commands);
    }
} 