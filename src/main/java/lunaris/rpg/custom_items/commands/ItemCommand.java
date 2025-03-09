package lunaris.rpg.custom_items.commands;

import lunaris.core.Lunaris;
import lunaris.core.command.BaseCommand;
import lunaris.core.command.CommandCategory;
import lunaris.core.command.CommandInfo;
import lunaris.core.rank.Rank;
import lunaris.rpg.custom_items.RPGItem;
import lunaris.rpg.custom_items.registry.ItemRegistry;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;


@CommandInfo(
    name = "esyalar",
    description = "RPG eşyalarını yönetin",
    allowedRanks = {Rank.YONETICI},
    category = CommandCategory.ADMIN
)
public class ItemCommand extends BaseCommand {
    
    public ItemCommand(Lunaris plugin) {
        super(plugin);
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length == 0) {
            sendItemList(player);
            return;
        }

        String itemId = args[0].toLowerCase();
        RPGItem item = ItemRegistry.getItem(itemId);
        
        if (item == null) {
            player.sendMessage("§cBöyle bir eşya bulunamadı!");
            return;
        }

        try {
            item.give(player);
            player.sendMessage("§aEşya envanterinize eklendi: §f" + itemId);
        } catch (Exception e) {
            player.sendMessage("§cEşya verilirken bir hata oluştu!");
            e.printStackTrace();
        }
    }

    private void sendItemList(Player player) {
        player.sendMessage("§6§lKayıtlı Eşyalar:");
        player.sendMessage("");

        for (RPGItem item : ItemRegistry.getAllItems()) {
            TextComponent message = new TextComponent("§7• §f" + item.getId());
            message.setClickEvent(new ClickEvent(
                ClickEvent.Action.RUN_COMMAND, 
                "/esyalar " + item.getId()
            ));
            message.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§7Eşyayı almak için tıkla!").create()
            ));
            
            player.spigot().sendMessage(message);
        }
    }
} 