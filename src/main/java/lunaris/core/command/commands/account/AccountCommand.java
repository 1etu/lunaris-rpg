package lunaris.core.command.commands.account;

import lunaris.core.Lunaris;
import lunaris.core.command.BaseCommand;
import lunaris.core.command.CommandCategory;
import lunaris.core.command.CommandInfo;
import lunaris.core.command.commands.account.ranks.RankCommand;
import lunaris.core.command.commands.account.levels.LevelCommand;
import lunaris.core.command.commands.account.kudret.KudretCommand;
import lunaris.core.rank.Rank;

import org.bukkit.entity.Player;

@CommandInfo(
    name = "hesap",
    description = "Oyuncu hesaplarını yönetin",
    aliases = {"oyuncu"},
    allowedRanks = {Rank.YONETICI},
    category = CommandCategory.ADMIN
)
public class AccountCommand extends BaseCommand {
    private final RankCommand rankCommand;
    private final LevelCommand levelCommand;
    private final KudretCommand kudretCommand;
    
    public AccountCommand(Lunaris plugin) {
        super(plugin);
        this.rankCommand = new RankCommand(plugin);
        this.levelCommand = new LevelCommand(plugin);
        this.kudretCommand = new KudretCommand(plugin);
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length == 0) {
            sendHelp(player);
            return;
        }

        String category = args[0].toLowerCase();
        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, args.length - 1);

        switch (category) {
            case "yetki":
                rankCommand.execute(player, newArgs);
                break;
            case "seviye":
                levelCommand.execute(player, newArgs);
                break;
            case "kudret":
                kudretCommand.execute(player, newArgs);
                break;
            default:
                sendHelp(player);
        }
    }

    private void sendHelp(Player player) {
        player.sendMessage("§6Hesap Yönetimi:");
        player.sendMessage("§7/hesap yetki <oyuncu> <rütbe> §8- §7Oyuncunun rütbesini değiştirir");
        player.sendMessage("§7/hesap seviye <oyuncu> <seviye> §8- §7Oyuncunun seviyesini değiştirir");
        player.sendMessage("§7/hesap kudret <oyuncu> <miktar> §8- §7Oyuncuya kudret ekler");
    }
} 