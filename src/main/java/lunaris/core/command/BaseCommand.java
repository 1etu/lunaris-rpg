package lunaris.core.command;

import lunaris.core.Lunaris;
import lunaris.core.command.commands.CommandManagerCommand;
import lunaris.core.rank.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.*;

public abstract class BaseCommand implements CommandExecutor, TabCompleter {
    protected final Lunaris plugin;
    private final Map<String, Method> subCommands = new HashMap<>();
    private final Map<String, SubCommandInfo> subCommandInfos = new HashMap<>();

    public BaseCommand(Lunaris plugin) {
        this.plugin = plugin;
        registerSubCommands();
    }

    private void registerSubCommands() {
        for (Method method : this.getClass().getDeclaredMethods()) {
            SubCommandInfo subInfo = method.getAnnotation(SubCommandInfo.class);
            if (subInfo != null) {
                subCommands.put(subInfo.name().toLowerCase(), method);
                subCommandInfos.put(subInfo.name().toLowerCase(), subInfo);
                for (String alias : subInfo.aliases()) {
                    subCommands.put(alias.toLowerCase(), method);
                    subCommandInfos.put(alias.toLowerCase(), subInfo);
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(":D");
            return true;
        }

        Player player = (Player) sender;
        CommandInfo info = this.getClass().getAnnotation(CommandInfo.class);
        
        if (CommandManagerCommand.isCommandDisabled(info.name()) && !player.hasPermission("lunaris.bypass.maintenance")) {
            player.sendMessage(CommandManagerCommand.getMaintenanceMessage());
            return true;
        }

        if (!hasPermission(player, info.allowedRanks())) {
            player.sendMessage("§cBu komutu kullanmak için yeterince yetkiniz yok!");
            return true;
        }

        if (args.length == 0) {
            execute(player, args);
            return true;
        }

        String subCommand = args[0].toLowerCase();
        Method method = subCommands.get(subCommand);
        
        if (method != null) {
            SubCommandInfo subInfo = subCommandInfos.get(subCommand);
            if (!hasPermission(player, subInfo.allowedRanks())) {
                player.sendMessage("§cBu komutu kullanmak için yeterince yetkiniz yok!");
                return true;
            }

            try {
                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                method.invoke(this, player, subArgs);
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage("§cBir hata oluştu!");
            }
        } else {
            execute(player, args);
        }

        return true;
    }

    protected abstract void execute(Player player, String[] args);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) return null;
        
        Player player = (Player) sender;
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            for (Map.Entry<String, SubCommandInfo> entry : subCommandInfos.entrySet()) {
                if (hasPermission(player, entry.getValue().allowedRanks())) {
                    completions.add(entry.getKey());
                }
            }
        }

        return completions;
    }

    protected boolean hasPermission(Player player, Rank[] requiredRanks) {
        if (requiredRanks.length == 0) {
            return true;
        }

        Rank playerRank = plugin.getRankManager().getPlayerRank(player);
        
        for (Rank required : requiredRanks) {
            if (playerRank.isAtLeast(required)) {
                return true;
            }
        }
        
        return false;
    }
} 