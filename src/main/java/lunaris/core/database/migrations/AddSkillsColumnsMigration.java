package lunaris.core.database.migrations;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AddSkillsColumnsMigration {
    public static void migrate(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String[] alterStatements = {
                "ALTER TABLE accounts ADD COLUMN class VARCHAR(32) DEFAULT 'NONE'",
                "ALTER TABLE accounts ADD COLUMN combat_level INT DEFAULT 0",
                "ALTER TABLE accounts ADD COLUMN woodcutting_level INT DEFAULT 0",
                "ALTER TABLE accounts ADD COLUMN mining_level INT DEFAULT 0",
                "ALTER TABLE accounts ADD COLUMN farming_level INT DEFAULT 0",
                "ALTER TABLE accounts ADD COLUMN fishing_level INT DEFAULT 0",
                "ALTER TABLE accounts ADD COLUMN cooking_level INT DEFAULT 0",
                "ALTER TABLE accounts ADD COLUMN alchemism_level INT DEFAULT 0",
                "ALTER TABLE accounts ADD COLUMN armouring_level INT DEFAULT 0",
                "ALTER TABLE accounts ADD COLUMN weaponsmithing_level INT DEFAULT 0",
                "ALTER TABLE accounts ADD COLUMN woodworking_level INT DEFAULT 0",
                "ALTER TABLE accounts ADD COLUMN health INT DEFAULT 100",
                "ALTER TABLE accounts ADD COLUMN defense INT DEFAULT 0",
                "ALTER TABLE accounts ADD COLUMN intelligence INT DEFAULT 0"
            };

            for (String sql : alterStatements) {
                try {
                    statement.execute(sql);
                } catch (SQLException e) {
                    if (!e.getMessage().contains("duplicate column name")) {
                        throw e;
                    }
                }
            }
        }
    }
} 