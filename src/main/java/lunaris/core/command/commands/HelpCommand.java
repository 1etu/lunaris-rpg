package lunaris.core.command.commands;

import lunaris.core.Lunaris;
import lunaris.core.command.BaseCommand;
import lunaris.core.command.CommandCategory;
import lunaris.core.command.CommandInfo;
import lunaris.core.utils.MessageFactory;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@CommandInfo(
    name = "komutlar",
    description = "Mevcut komutları görüntüleyin",
    allowedRanks = {}
)
public class HelpCommand extends BaseCommand {
    private final Map<CommandCategory, List<BaseCommand>> categorizedCommands = new EnumMap<>(CommandCategory.class);
    
    public HelpCommand(Lunaris plugin) {
        super(plugin);
        categorizeCommands();
    }

    private void categorizeCommands() {
        for (CommandCategory category : CommandCategory.values()) {
            categorizedCommands.put(category, new ArrayList<>());
        }

        plugin.getCommandManager().getCommands().forEach(command -> {
            CommandInfo info = command.getClass().getAnnotation(CommandInfo.class);
            if (info != null) {
                categorizedCommands.get(info.category()).add(command);
            }
        });
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length == 0) {
            sendCategories(player);
        } else {
            try {
                CommandCategory category = CommandCategory.valueOf(args[0].toUpperCase());
                sendCategoryCommands(player, category);
            } catch (IllegalArgumentException e) {
                player.sendMessage(MessageFactory.error("Geçersiz bir kategori girdiniz!"));
            }
        }
    }

    private void sendCategories(Player player) {
        player.sendMessage(MessageFactory.header("Komut Kategorileri"));
        
        for (CommandCategory category : CommandCategory.values()) {
            List<BaseCommand> commands = categorizedCommands.get(category);
            List<BaseCommand> accessibleCommands = commands.stream()
                .filter(cmd -> hasPermission(player, cmd.getClass().getAnnotation(CommandInfo.class).allowedRanks()))
                .collect(Collectors.toList());

            if (!accessibleCommands.isEmpty()) {
                TextComponent message = new TextComponent(category.getColor() + "➤ " + category.getName());
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/komutlar " + category.name()));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
                    new ComponentBuilder("§7" + category.getName() + " komutlarını görüntülemek için tıkla").create()));
                
                player.spigot().sendMessage(message);
            }
        }
    }

    private void sendCategoryCommands(Player player, CommandCategory category) {
        List<BaseCommand> commands = categorizedCommands.get(category);
        List<BaseCommand> accessibleCommands = commands.stream()
            .filter(cmd -> hasPermission(player, cmd.getClass().getAnnotation(CommandInfo.class).allowedRanks()))
            .collect(Collectors.toList());

        if (accessibleCommands.isEmpty()) {
            player.sendMessage(MessageFactory.error("Bu kategoride kullanabileceğiniz komut bulunmuyor!"));
            return;
        }

        player.sendMessage(MessageFactory.coloredHeader(category.getName() + " Komutları", category.getColor()));
        
        for (BaseCommand command : accessibleCommands) {
            CommandInfo info = command.getClass().getAnnotation(CommandInfo.class);
            
            TextComponent message = new TextComponent("§7/" + info.name());
            message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + info.name() + " "));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
                new ComponentBuilder("§7" + info.description() + "\n§eKomutu kullanmak için tıkla").create()));
            
            player.spigot().sendMessage(message);
        }
    }
} 