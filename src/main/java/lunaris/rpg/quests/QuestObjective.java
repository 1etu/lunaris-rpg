package lunaris.rpg.quests;

import org.bukkit.entity.Player;

public abstract class QuestObjective {
    private final String description;
    private final int requiredProgress;
    private final ObjectiveType type;
    
    protected QuestObjective(String description, int requiredProgress, ObjectiveType type) {
        this.description = description;
        this.requiredProgress = requiredProgress;
        this.type = type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getRequiredProgress() {
        return requiredProgress;
    }
    
    public ObjectiveType getType() {
        return type;
    }
    
    public abstract boolean isComplete(Player player, PlayerQuest playerQuest);
    
    public abstract void onProgress(Player player, PlayerQuest playerQuest);
} 