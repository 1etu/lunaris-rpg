package lunaris.core.command.commands.account.kudret;

import lunaris.core.Lunaris;
import lunaris.core.account.AccountModule;
import lunaris.core.command.BaseCommand;
import lunaris.core.utils.MessageFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class KudretCommand extends BaseCommand {
    private final Lunaris plugin;

    public KudretCommand(Lunaris plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(MessageFactory.error("Kullanım: /hesap kudret <oyuncu> <miktar>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(MessageFactory.error("Oyuncu bulunamadı!"));
            return;
        }

        try {
            int amount = Integer.parseInt(args[1]);
            if (amount < 0) {
                player.sendMessage(MessageFactory.error("Kudret miktarı 0'dan küçük olamaz!"));
                return;
            }

            addKudret(player, target, amount);

        } catch (NumberFormatException e) {
            player.sendMessage(MessageFactory.error("Geçerli bir miktar giriniz!"));
        }
    }

    private void addKudret(Player admin, Player target, int amount) {
        plugin.getModuleManager().getModule(AccountModule.class).ifPresent(accountModule -> {
            accountModule.getXPManager().addXP(target, amount);
            
            admin.sendMessage(MessageFactory.success(target.getName() + " adlı oyuncuya " + amount + " Kudret eklendi."));
        });
    }
} 