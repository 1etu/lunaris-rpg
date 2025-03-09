package lunaris.core.utils;

import lunaris.core.account.Account;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;


public class PlayerDisplayManager {
    public static void updatePlayerDisplays(Player player, Account account) {
        Scoreboard scoreboard;
        fr.minuskube.netherboard.bukkit.BPlayerBoard playerBoard = fr.minuskube.netherboard.Netherboard.instance().getBoard(player);
        if (playerBoard != null) {
            scoreboard = playerBoard.getScoreboard();
        } else {
            scoreboard = player.getServer().getScoreboardManager().getMainScoreboard();
        }
        
        String teamName = "nt_" + player.getName();
        
        Team team = scoreboard.getTeam(teamName);
        if (team != null) {
            team.unregister();
        }
        
        team = scoreboard.registerNewTeam(teamName);
        
        String prefix = account.getLevelColor() + "[" + account.getLevel() + "] ";
        if (account.getPlayerClass() != null && account.getPlayerClass().getIcon() != null) {
            prefix += account.getPlayerClass().getIcon() + " ";
        }

        team.setPrefix(prefix);
        team.setSuffix(" " + account.getRank().getFormattedName());
        team.setColor(ChatColor.valueOf(account.getRank().getColor().getName().toUpperCase()));
        team.addEntry(player.getName());
        
        String tabName = account.getLevelColor() + "[" + account.getLevel() + "] " +
                        (account.getPlayerClass().getIcon() != null ? account.getPlayerClass().getIcon() + " " : "") +
                        account.getRank().getFormattedName() + " " +
                        account.getRank().getColor() + player.getName();
        player.setPlayerListName(tabName);
        
        for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
            onlinePlayer.setScoreboard(scoreboard);
        }
    }
} 