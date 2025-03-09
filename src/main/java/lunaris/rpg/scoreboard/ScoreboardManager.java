package lunaris.rpg.scoreboard;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import lunaris.core.Lunaris;
import lunaris.rpg.quests.Quest;
import lunaris.rpg.quests.QuestManager;
import lunaris.rpg.quests.QuestObjective;
import lunaris.rpg.quests.PlayerQuest;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScoreboardManager {
    private static final String TITLE = "§6§lplay.lunaris.com §7(RPG)";
    private final Lunaris plugin;
    
    public ScoreboardManager(Lunaris plugin) {
        this.plugin = plugin;
    }
    
    public void updateScoreboard(Player player) {
        BPlayerBoard board = Netherboard.instance().getBoard(player);
        if (board == null) {
            board = Netherboard.instance().createBoard(player, TITLE);
        }
        
        List<String> lines = new ArrayList<>();
        lines.add("");
        
        Optional<PlayerQuest> activeQuest = plugin.getModuleManager()
            .getModule(QuestManager.class)
            .flatMap(module -> module.getActiveQuest(player));
            
        if (activeQuest.isPresent()) {
            Quest quest = plugin.getModuleManager()
                .getModule(QuestManager.class)
                .flatMap(module -> module.getQuest(activeQuest.get().getQuestId()))
                .orElse(null);
                
            if (quest != null) {
                addQuestInfo(lines, quest, activeQuest.get());
            }
        }
        
        lines.add("§b");
        
        updateLines(board, lines);
    }
    
    private void addQuestInfo(List<String> lines, Quest quest, PlayerQuest playerQuest) {
        lines.add("§b§lMevcut Görev:");
        lines.add("§f" + quest.getTitle());
        
        String description = quest.getDescription();
        if (description.length() > 30) {
            String[] words = description.split(" ");
            StringBuilder line = new StringBuilder();
            for (String word : words) {
                if (line.length() + word.length() > 30) {
                    lines.add("§7" + line.toString().trim());
                    line = new StringBuilder();
                }
                line.append(word).append(" ");
            }
            if (line.length() > 0) {
                lines.add("§7" + line.toString().trim());
            }
        } else {
            lines.add("§7" + description);
        }
        
        lines.add("§c");
        
        if (quest.getHint() != null && !quest.getHint().isEmpty()) {
            lines.add("§c§lİpucu: §f");
            lines.add("§f" + quest.getHint());
            lines.add("§d");
        }
        
        if (!quest.getObjectives().isEmpty()) {
            lines.add("§a§lHedefler:");
            for (int i = 0; i < quest.getObjectives().size(); i++) {
                QuestObjective objective = quest.getObjectives().get(i);
                int progress = playerQuest.getObjectiveProgress(i);
                int required = objective.getRequiredProgress();
                
                lines.add(String.format("§a- §7%s: §f%d§7/%d",
                    objective.getDescription(),
                    progress,
                    required
                ));
            }
        }
    }
    
    private void updateLines(BPlayerBoard board, List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            board.set(lines.get(lines.size() - 1 - i), i + 1);
        }
        
        for (int i = lines.size() + 1; i <= 15; i++) {
            board.remove(i);
        }
    }
} 