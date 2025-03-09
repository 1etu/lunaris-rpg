package lunaris.core.database.migrations;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateProfilesTableMigration {
    public static void migrate(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                CREATE TABLE IF NOT EXISTS profiles (
                    profile_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    account_id INTEGER NOT NULL,
                    uuid VARCHAR(36) NOT NULL,
                    name VARCHAR(32) NOT NULL,
                    creation_date TIMESTAMP NOT NULL,
                    is_locked BOOLEAN NOT NULL DEFAULT 0,
                    playtime_minutes INTEGER NOT NULL DEFAULT 0,
                    last_login TIMESTAMP,
                    FOREIGN KEY (account_id) REFERENCES accounts(account_id),
                    UNIQUE(uuid)
                )
            """);
        }
    }
} 