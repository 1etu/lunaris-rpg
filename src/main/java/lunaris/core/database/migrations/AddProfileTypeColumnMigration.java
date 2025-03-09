package lunaris.core.database.migrations;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AddProfileTypeColumnMigration {
    public static void migrate(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            try {
                statement.executeQuery("SELECT profile_type FROM profiles LIMIT 1");
            } catch (SQLException e) {
                statement.executeUpdate(
                    "ALTER TABLE profiles " +
                    "ADD COLUMN profile_type VARCHAR(20) NOT NULL DEFAULT 'TEKIL'"
                );
            }
        }
    }
} 