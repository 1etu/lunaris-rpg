package lunaris.core.command.commands;

import lunaris.core.Lunaris;
import lunaris.core.command.BaseCommand;
import lunaris.core.command.CommandCategory;
import lunaris.core.command.CommandInfo;
import lunaris.core.command.SubCommandInfo;
import lunaris.core.rank.Rank;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@CommandInfo(
    name = "komut",
    description = "Komutları yönetin",
    aliases = {"cmd"},
    allowedRanks = {Rank.YONETICI},
    category = CommandCategory.ADMIN
)
public class CommandManagerCommand extends BaseCommand {
    private static final Set<String> disabledCommands = new HashSet<>();
    private static final String MAINTENANCE_MESSAGE = "§c⚠ Bu komut şu anda bakım modundadır.";
    
    public CommandManagerCommand(Lunaris plugin) {
        super(plugin);
    }

    @Override
    protected void execute(Player player, String[] args) {
        player.sendMessage("§6Komut Yönetimi:");
        player.sendMessage("§7/komut <komut> devre-disi §8- §7Bir komutu devre dışı bırak");
        player.sendMessage("§7/komut <komut> etkinlestir §8- §7Bir komutu etkinleştir");
        player.sendMessage("§7/komut liste §8- §7Devre dışı komutları listele");
    }

    @SubCommandInfo(
        name = "devre-disi",
        description = "Bir komutu devre dışı bırak",
        allowedRanks = {Rank.YONETICI}
    )
    public void disableCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("§cKullanım: /komut <komut> devre-disi");
            return;
        }

        String commandName = args[0].toLowerCase();
        if (commandName.equals("komut") || commandName.equals("komutlar")) {
            player.sendMessage("§cBu komutu devre dışı bırakamazsınız!");
            return;
        }

        disabledCommands.add(commandName);
        player.sendMessage("§a'" + commandName + "' komutu devre dışı bırakıldı.");
    }

    @SubCommandInfo(
        name = "etkinlestir",
        description = "Bir komutu etkinleştir",
        allowedRanks = {Rank.YONETICI}
    )
    public void enableCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("§cKullanım: /komut <komut> etkinlestir");
            return;
        }

        String commandName = args[0].toLowerCase();
        if (disabledCommands.remove(commandName)) {
            player.sendMessage("§a'" + commandName + "' komutu etkinleştirildi.");
        } else {
            player.sendMessage("§cBu komut zaten devre dışı değil!");
        }
    }

    @SubCommandInfo(
        name = "liste",
        description = "Devre dışı komutları listele",
        allowedRanks = {Rank.YONETICI}
    )
    public void listDisabled(Player player, String[] args) {
        if (disabledCommands.isEmpty()) {
            player.sendMessage("§aŞu anda devre dışı bırakılmış komut bulunmamaktadır.");
            return;
        }

        player.sendMessage("§6Devre Dışı Komutlar:");
        disabledCommands.forEach(cmd -> player.sendMessage("§7- " + cmd));
    }

    public static boolean isCommandDisabled(String commandName) {
        return disabledCommands.contains(commandName.toLowerCase());
    }

    public static String getMaintenanceMessage() {
        return MAINTENANCE_MESSAGE;
    }
} 