package lunaris.core.command.commands.account.levels;

import lunaris.core.Lunaris;
import lunaris.core.account.Account;
import lunaris.core.account.AccountModule;
import lunaris.core.command.SubCommandInfo;
import lunaris.core.rank.Rank;
import lunaris.core.utils.MessageFactory;
import lunaris.core.utils.UsageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LevelCommand {
    private final Lunaris plugin;

    public LevelCommand(Lunaris plugin) {
        this.plugin = plugin;
    }

    @SubCommandInfo(
        name = "seviye",
        description = "Oyuncuların seviyelerini yönetir",
        allowedRanks = {Rank.YONETICI}
    )
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(MessageFactory.error("Kullanım: " + 
                UsageBuilder.buildUsage("hesap seviye", "oyuncu", "seviye")));
            return;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage(MessageFactory.error("Oyuncu bulunamadı!"));
            return;
        }

        try {
            int newLevel = Integer.parseInt(args[1]);
            if (newLevel < 0) {
                player.sendMessage(MessageFactory.error("Seviye 0'dan küçük olamaz!"));
                return;
            }

            setLevel(player, target, newLevel);

        } catch (NumberFormatException e) {
            player.sendMessage(MessageFactory.error("Geçerli bir seviye giriniz!"));
        }
    }

    private void setLevel(Player admin, Player target, int newLevel) {
        plugin.getModuleManager().getModule(AccountModule.class).ifPresent(accountModule -> {
            Account account = accountModule.getAccountService().getOrCreateAccount(target);
            int oldLevel = account.getLevel();
            
            accountModule.getAccountService().updateLevel(target.getUniqueId(), newLevel);
            
            String logMessage = String.format(
                "§7[Seviye] %s, %s adlı oyuncunun seviyesini %d seviyesinden %d seviyesine değiştirdi",
                admin.getName(),
                target.getName(),
                oldLevel,
                newLevel
            );
            
            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> plugin.getRankManager().getPlayerRank(p).isAtLeast(Rank.YONETICI))
                    .forEach(p -> p.sendMessage(logMessage));
        });
    }
} 