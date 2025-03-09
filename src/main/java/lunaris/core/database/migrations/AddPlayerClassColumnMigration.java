package lunaris.core.database.migrations;

import java.sql.Connection;
import java.sql.SQLException;

public class AddPlayerClassColumnMigration {
    public static void migrate(Connection connection) throws SQLException {
        try {
            connection.createStatement().execute(
                "SELECT player_class FROM accounts LIMIT 1"
            );
        } catch (SQLException e) {
            connection.createStatement().execute(
                "ALTER TABLE accounts ADD COLUMN player_class VARCHAR(50) DEFAULT 'NONE'"
            );
        }
    }
} 