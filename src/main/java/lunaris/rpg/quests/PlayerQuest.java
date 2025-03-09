package lunaris.rpg.quests;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerQuest {
    private final UUID playerUuid;
    private final int questId;
    private final LocalDateTime startTime;
    private QuestStatus status;
    private final Map<Integer, Integer> objectiveProgress;
    
    public PlayerQuest(UUID playerUuid, int questId) {
        this.playerUuid = playerUuid;
        this.questId = questId;
        this.startTime = LocalDateTime.now();
        this.status = QuestStatus.IN_PROGRESS;
        this.objectiveProgress = new HashMap<>();
    }
    
    public UUID getPlayerUuid() {
        return playerUuid;
    }
    
    public int getQuestId() {
        return questId;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public QuestStatus getStatus() {
        return status;
    }
    
    public void setStatus(QuestStatus status) {
        this.status = status;
    }
    
    public int getObjectiveProgress(int objectiveIndex) {
        return objectiveProgress.getOrDefault(objectiveIndex, 0);
    }
    
    public void incrementObjectiveProgress(int objectiveIndex) {
        objectiveProgress.merge(objectiveIndex, 1, Integer::sum);
    }
    
    public void setObjectiveProgress(int objectiveIndex, int progress) {
        objectiveProgress.put(objectiveIndex, progress);
    }
} 