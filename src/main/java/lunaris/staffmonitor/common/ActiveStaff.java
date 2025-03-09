package lunaris.staffmonitor.common;

import lunaris.core.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ActiveStaff {

    public static List<Player> getActiveStaff() {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> {
                    Rank rank = Rank.valueOf(player.getMetadata("rank").get(0).asString());
                    return rank.isAtLeast(Rank.MODERATOR);
                })
                .collect(Collectors.toList());
    }
}
