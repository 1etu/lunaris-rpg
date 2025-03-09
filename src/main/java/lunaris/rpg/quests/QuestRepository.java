package lunaris.rpg.quests;

import lunaris.core.Lunaris;
import lunaris.core.database.Repository;
import lunaris.rpg.classes.PlayerClass;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestRepository extends Repository {
    private final Lunaris plugin;
    
    public QuestRepository(Lunaris plugin) {
        super(plugin);
        this.plugin = plugin;
        try {
            initialize();
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void createTables() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                CREATE TABLE IF NOT EXISTS quests (
                    quest_id INT PRIMARY KEY,
                    title VARCHAR(100) NOT NULL,
                    description TEXT NOT NULL,
                    hint TEXT,
                    required_class VARCHAR(50),
                    required_level INT DEFAULT 1,
                    rewards TEXT
                )
            """);
            
            statement.execute("""
                CREATE TABLE IF NOT EXISTS quest_prerequisites (
                    quest_id INT,
                    prerequisite_quest_id INT,
                    PRIMARY KEY (quest_id, prerequisite_quest_id),
                    FOREIGN KEY (quest_id) REFERENCES quests(quest_id),
                    FOREIGN KEY (prerequisite_quest_id) REFERENCES quests(quest_id)
                )
            """);
            
            statement.execute("""
                CREATE TABLE IF NOT EXISTS player_quests (
                    player_uuid VARCHAR(36),
                    quest_id INT,
                    status VARCHAR(20) NOT NULL,
                    start_time TIMESTAMP NOT NULL,
                    completion_time TIMESTAMP,
                    PRIMARY KEY (player_uuid, quest_id),
                    FOREIGN KEY (quest_id) REFERENCES quests(quest_id)
                )
            """);
            
            statement.execute("""
                CREATE TABLE IF NOT EXISTS quest_objectives (
                    quest_id INT,
                    objective_index INT,
                    type VARCHAR(50) NOT NULL,
                    description TEXT NOT NULL,
                    required_progress INT DEFAULT 1,
                    PRIMARY KEY (quest_id, objective_index),
                    FOREIGN KEY (quest_id) REFERENCES quests(quest_id)
                )
            """);
            
            statement.execute("""
                CREATE TABLE IF NOT EXISTS player_objective_progress (
                    player_uuid VARCHAR(36),
                    quest_id INT,
                    objective_index INT,
                    progress INT DEFAULT 0,
                    PRIMARY KEY (player_uuid, quest_id, objective_index),
                    FOREIGN KEY (quest_id, objective_index) 
                        REFERENCES quest_objectives(quest_id, objective_index)
                )
            """);
        }
    }
    
    public List<Quest> getAllQuests() throws SQLException {
        List<Quest> quests = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(
                 "SELECT * FROM quests ORDER BY quest_id")) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Quest quest = new Quest.Builder()
                    .questId(rs.getInt("quest_id"))
                    .title(rs.getString("title"))
                    .description(rs.getString("description"))
                    .hint(rs.getString("hint"))
                    .requiredClass(PlayerClass.fromString(rs.getString("required_class")))
                    .requiredLevel(rs.getInt("required_level"))
                    .build();
                    
                quests.add(quest);
            }
        }
        
        return quests;
    }
    
    public void savePlayerQuest(PlayerQuest playerQuest) throws SQLException {
        String sql = """
            UPDATE player_quests SET 
                status = ?,
                completion_time = CASE 
                    WHEN ? = 'COMPLETED' THEN CURRENT_TIMESTAMP
                    ELSE NULL
                END
            WHERE player_uuid = ? AND quest_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, playerQuest.getStatus().name());
            stmt.setString(2, playerQuest.getStatus().name());
            stmt.setString(3, playerQuest.getPlayerUuid().toString());
            stmt.setInt(4, playerQuest.getQuestId());
            
            stmt.executeUpdate();
        }
    }
    
    public boolean isQuestCompleted(UUID playerUuid, int questId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("""
            SELECT status FROM player_quests 
            WHERE player_uuid = ? AND quest_id = ?
        """)) {
            
            stmt.setString(1, playerUuid.toString());
            stmt.setInt(2, questId);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getString("status").equals(QuestStatus.COMPLETED.name());
        }
    }
} 