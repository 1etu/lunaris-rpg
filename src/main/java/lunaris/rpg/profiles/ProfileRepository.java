package lunaris.rpg.profiles;

import lunaris.core.Lunaris;
import lunaris.core.database.DatabaseModule;
import lunaris.core.database.ProfileDatabaseRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProfileRepository {
    private final Lunaris plugin;
    private final ProfileDatabaseRepository databaseRepository;

    public ProfileRepository(Lunaris plugin) {
        this.plugin = plugin;
        this.databaseRepository = plugin.getModuleManager()
            .getModule(DatabaseModule.class)
            .map(module -> ((DatabaseModule) module).getProfileRepository())
            .orElseThrow(() -> new IllegalStateException("db_module_not_found"));
    }

    public Profile save(Profile profile) throws SQLException {
        return databaseRepository.save(profile);
    }

    public Optional<Profile> findByUUID(UUID uuid) throws SQLException {
        return databaseRepository.findByUUID(uuid);
    }

    public List<Profile> findByAccountId(int accountId) throws SQLException {
        return databaseRepository.findByAccountId(accountId);
    }

    public void delete(UUID uuid) throws SQLException {
        databaseRepository.delete(uuid);
    }

    public void updatePlaytime(UUID uuid, int additionalMinutes) throws SQLException {
        databaseRepository.updatePlaytime(uuid, additionalMinutes);
    }
} 