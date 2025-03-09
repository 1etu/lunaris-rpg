package lunaris.core.database.migrations;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AddMissingStatsColumnsMigration {
    public static void migrate(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String[] alterStatements = {
                "ALTER TABLE accounts ADD COLUMN xp INT DEFAULT 0",
                "ALTER TABLE accounts ADD COLUMN health INT DEFAULT 100",
                "ALTER TABLE accounts ADD COLUMN defense INT DEFAULT 10",
                "ALTER TABLE accounts ADD COLUMN intelligence INT DEFAULT 10"
            };

            for (String sql : alterStatements) {
                try {
                    statement.execute(sql);
                } catch (SQLException e) {
                    if (!e.getMessage().contains("duplicate column")) {
                        throw e;
                    }
                }
            }
        }
    }
} 