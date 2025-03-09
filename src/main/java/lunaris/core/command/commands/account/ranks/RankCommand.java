package lunaris.core.command.commands.account.ranks;

import lunaris.core.Lunaris;
import lunaris.core.account.Account;
import lunaris.core.account.AccountModule;
import lunaris.core.command.SubCommandInfo;
import lunaris.core.rank.Rank;
import lunaris.core.utils.MessageFactory;
import lunaris.core.utils.UsageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RankCommand {
    private final Lunaris plugin;

    public RankCommand(Lunaris plugin) {
        this.plugin = plugin;
    }

    @SubCommandInfo(
        name = "yetki",
        description = "Oyuncuların yetkilerini yönetir",
        allowedRanks = {Rank.YONETICI}
    )
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(MessageFactory.error("Kullanım: " + 
                UsageBuilder.buildUsage("hesap yetki", "oyuncu", "rütbe")));
            return;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage(MessageFactory.error("Oyuncu bulunamadı!"));
            return;
        }

        try {
            Rank newRank = Rank.valueOf(args[1].toUpperCase());
            setRank(player, target, newRank);

        } catch (IllegalArgumentException e) {
            player.sendMessage(MessageFactory.error("Geçersiz rütbe! Mevcut rütbeler:"));
            for (Rank rank : Rank.values()) {
                if (rank != null) {
                    player.sendMessage("§7- " + rank.name());
                }
            }
        }
    }

    private void handleHistory(Player player, String targetName) {
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage(":(");
            return;
        }

        // TOOD
    }

    private void setRank(Player admin, Player target, Rank newRank) {
        plugin.getModuleManager().getModule(AccountModule.class).ifPresent(accountModule -> {
            Account account = accountModule.getAccountService().getOrCreateAccount(target);
            Rank oldRank = account.getRank();
            
            accountModule.getAccountService().updateRank(target.getUniqueId(), newRank);
            
            logRankChange(admin, target, oldRank, newRank);
            
            admin.sendMessage(MessageFactory.success(target.getName() + " adlı oyuncunun rütbesi " + 
                newRank.getFormattedName() + " §7olarak güncellendi."));
            target.sendMessage(MessageFactory.success("Rütbeniz " + newRank.getFormattedName() + 
                " §7olarak güncellendi."));
        });
    }

    private void logRankChange(Player admin, Player target, Rank oldRank, Rank newRank) {
        String logMessage = String.format(
            "§7[Rütbe] %s, %s adlı oyuncunun rütbesini %s rütbesinden %s rütbesine değiştirdi",
            admin.getName(),
            target.getName(),
            oldRank.getFormattedName(),
            newRank.getFormattedName()
        );
        
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> plugin.getRankManager().getPlayerRank(p).isAtLeast(Rank.YONETICI))
                .forEach(p -> p.sendMessage(logMessage));
    }
} 