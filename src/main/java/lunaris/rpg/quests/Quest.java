package lunaris.rpg.quests;

import lunaris.rpg.classes.PlayerClass;

import java.util.ArrayList;
import java.util.List;

public class Quest {
    private final int questId;
    private final String title;
    private final String description;
    private final String hint;
    private final List<QuestObjective> objectives;
    private final PlayerClass requiredClass;
    private final int requiredLevel;
    private final List<Integer> prerequisiteQuestIds;
    private final QuestRewards rewards;
    
    private Quest(Builder builder) {
        this.questId = builder.questId;
        this.title = builder.title;
        this.description = builder.description;
        this.hint = builder.hint;
        this.objectives = builder.objectives;
        this.requiredClass = builder.requiredClass;
        this.requiredLevel = builder.requiredLevel;
        this.prerequisiteQuestIds = builder.prerequisiteQuestIds;
        this.rewards = builder.rewards;
    }
    
    public int getQuestId() {
        return questId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getHint() {
        return hint;
    }
    
    public List<QuestObjective> getObjectives() {
        return objectives;
    }
    
    public PlayerClass getRequiredClass() {
        return requiredClass;
    }
    
    public int getRequiredLevel() {
        return requiredLevel;
    }
    
    public List<Integer> getPrerequisiteQuestIds() {
        return prerequisiteQuestIds;
    }
    
    public QuestRewards getRewards() {
        return rewards;
    }
    
    public static class Builder {
        private int questId;
        private String title;
        private String description;
        private String hint;
        private List<QuestObjective> objectives = new ArrayList<>();
        private PlayerClass requiredClass = PlayerClass.NONE;
        private int requiredLevel = 1;
        private List<Integer> prerequisiteQuestIds = new ArrayList<>();
        private QuestRewards rewards = new QuestRewards.Builder().build();
        
        public Builder questId(int questId) {
            this.questId = questId;
            return this;
        }
        
        public Builder title(String title) {
            this.title = title;
            return this;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder hint(String hint) {
            this.hint = hint;
            return this;
        }
        
        public Builder objective(QuestObjective objective) {
            this.objectives.add(objective);
            return this;
        }
        
        public Builder requiredClass(PlayerClass requiredClass) {
            this.requiredClass = requiredClass;
            return this;
        }
        
        public Builder requiredLevel(int requiredLevel) {
            this.requiredLevel = requiredLevel;
            return this;
        }
        
        public Builder prerequisiteQuest(int questId) {
            this.prerequisiteQuestIds.add(questId);
            return this;
        }
        
        public Builder rewards(QuestRewards rewards) {
            this.rewards = rewards;
            return this;
        }
        
        public Quest build() {
            return new Quest(this);
        }
    }
} 