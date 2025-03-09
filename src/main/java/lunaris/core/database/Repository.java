package lunaris.core.database;

import lunaris.core.Lunaris;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Repository implements AutoCloseable {
    protected final Lunaris plugin;
    protected Connection connection;

    public Repository(Lunaris plugin) {
        this.plugin = plugin;
    }

    protected void initialize() throws SQLException {
        File dataFolder = new File(plugin.getDataFolder(), "database");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        String url = "jdbc:sqlite:" + new File(dataFolder, "database.db");
        connection = DriverManager.getConnection(url);
    }

    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    protected abstract void createTables() throws SQLException;

    public Connection getConnection() {
        return connection;
    }
} 