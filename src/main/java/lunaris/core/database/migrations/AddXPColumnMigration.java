package lunaris.core.database.migrations;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AddXPColumnMigration {
    public static void migrate(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            try {
                statement.execute("SELECT xp FROM accounts LIMIT 1");
            } catch (SQLException e) {
                statement.execute("ALTER TABLE accounts ADD COLUMN xp INT NOT NULL DEFAULT 0");
            }
        }
    }
} 