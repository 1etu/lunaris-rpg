package lunaris.core.database.migrations;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AddAccountIdMigration {
    public static void migrate(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                ALTER TABLE accounts ADD COLUMN account_id INTEGER PRIMARY KEY AUTOINCREMENT
            """);
        } catch (SQLException e) {
            if (!e.getMessage().contains("duplicate column name")) {
                throw e;
            }
        }
    }
} 