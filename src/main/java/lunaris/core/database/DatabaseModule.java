package lunaris.core.database;

import lunaris.core.Lunaris;
import lunaris.core.database.migrations.AddSkillsColumnsMigration;
import lunaris.core.database.migrations.AddPlayerClassColumnMigration;
import lunaris.core.database.migrations.AddMissingStatsColumnsMigration;
import lunaris.core.module.IModule;
import lunaris.core.module.ModuleInfo;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@ModuleInfo(name = "Database")
public class DatabaseModule implements IModule {
    private final Lunaris plugin;
    private Connection connection;
    private ProfileDatabaseRepository profileRepository;

    public DatabaseModule(Lunaris plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        try {
            initializeDatabase();
            createTables(); // we should migrate here
            
            this.profileRepository = new ProfileDatabaseRepository(plugin);
        } catch (SQLException e) {
            plugin.getLogger().severe("[err->db] cant initialize" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("[err->db] cant close" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "Database";
    }

    @Override
    public ModuleInfo getInfo() {
        return getClass().getAnnotation(ModuleInfo.class);
    }

    private void initializeDatabase() throws SQLException {
        AccountRepository repository = new AccountRepository(plugin);
        this.connection = repository.getConnection();
    }

    private void createTables() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            AddSkillsColumnsMigration.migrate(connection);
            AddPlayerClassColumnMigration.migrate(connection);
            AddMissingStatsColumnsMigration.migrate(connection);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public ProfileDatabaseRepository getProfileRepository() {
        return profileRepository;
    }
} 