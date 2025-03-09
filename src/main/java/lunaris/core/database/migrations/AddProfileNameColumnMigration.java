package lunaris.core.database.migrations;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AddProfileNameColumnMigration {
    public static void migrate(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            try {
                statement.executeQuery("SELECT profile_name FROM profiles LIMIT 1");
            } catch (SQLException e) {
                statement.executeUpdate(
                    "ALTER TABLE profiles " +
                    "ADD COLUMN profile_name VARCHAR(16) NOT NULL DEFAULT ''"
                );
                
                statement.executeUpdate(
                    "UPDATE profiles SET profile_name = name WHERE profile_name = ''"
                );
            }
        }
    }
} 