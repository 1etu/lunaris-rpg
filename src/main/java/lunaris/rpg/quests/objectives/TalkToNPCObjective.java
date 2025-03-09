package lunaris.rpg.quests.objectives;

import lunaris.rpg.quests.ObjectiveType;
import lunaris.rpg.quests.PlayerQuest;
import lunaris.rpg.quests.QuestObjective;
import org.bukkit.entity.Player;

public class TalkToNPCObjective extends QuestObjective {
    private final String npcName;
    
    public TalkToNPCObjective(String npcName) {
        super(npcName + " ile konuş", 1, ObjectiveType.TALK_TO_NPC);
        this.npcName = npcName;
    }
    
    @Override
    public boolean isComplete(Player player, PlayerQuest playerQuest) {
        return playerQuest.getObjectiveProgress(0) >= getRequiredProgress();
    }
    
    @Override
    public void onProgress(Player player, PlayerQuest playerQuest) {
        playerQuest.incrementObjectiveProgress(0);
    }
    
    public String getNpcName() {
        return npcName;
    }
} 