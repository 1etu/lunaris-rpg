package lunaris.core.database;

import lunaris.core.Lunaris;
import lunaris.rpg.profiles.Profile;
import lunaris.core.database.migrations.CreateProfilesTableMigration;
import lunaris.rpg.profiles.ProfileType;
import lunaris.core.database.migrations.AddProfileTypeColumnMigration;
import lunaris.core.database.migrations.AddProfileNameColumnMigration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProfileDatabaseRepository extends Repository {
    
    public ProfileDatabaseRepository(Lunaris plugin) {
        super(plugin);
        try {
            initialize();
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void createTables() throws SQLException {
        CreateProfilesTableMigration.migrate(connection);
        AddProfileTypeColumnMigration.migrate(connection);
        AddProfileNameColumnMigration.migrate(connection);
    }

    public Profile save(Profile profile) throws SQLException {
        if (profile.getProfileId() == -1) {
            return insertProfile(profile);
        } else {
            return updateProfile(profile);
        }
    }

    private Profile insertProfile(Profile profile) throws SQLException {
        String sql = "INSERT INTO profiles (account_id, uuid, name, profile_name, creation_date, is_locked, " +
                    "playtime_minutes, last_login, profile_type) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setProfileStatementParameters(stmt, profile);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return findByUUID(profile.getUuid()).orElseThrow();
                } else {
                    throw new SQLException("no id, failed creation");
                }
            }
        }
    }

    private Profile updateProfile(Profile profile) throws SQLException {
        String sql = "UPDATE profiles SET account_id = ?, uuid = ?, name = ?, profile_name = ?, creation_date = ?, " +
                    "is_locked = ?, playtime_minutes = ?, last_login = ?, profile_type = ? " +
                    "WHERE profile_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setProfileStatementParameters(stmt, profile);
            stmt.setInt(10, profile.getProfileId());
            stmt.executeUpdate();
            return profile;
        }
    }

    private void setProfileStatementParameters(PreparedStatement stmt, Profile profile) throws SQLException {
        stmt.setInt(1, profile.getAccountId());
        stmt.setString(2, profile.getUuid().toString());
        stmt.setString(3, profile.getName());
        stmt.setString(4, profile.getProfileName());
        stmt.setTimestamp(5, Timestamp.valueOf(profile.getCreationDate()));
        stmt.setBoolean(6, profile.isLocked());
        stmt.setInt(7, profile.getPlaytimeMinutes());
        stmt.setTimestamp(8, Timestamp.valueOf(profile.getLastLogin()));
        stmt.setString(9, profile.getProfileType().name());
    }

    public Optional<Profile> findByUUID(UUID uuid) throws SQLException {
        String sql = "SELECT * FROM profiles WHERE uuid = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToProfile(rs));
            }
        }
        return Optional.empty();
    }

    public List<Profile> findByAccountId(int accountId) throws SQLException {
        String sql = "SELECT * FROM profiles WHERE account_id = ? ORDER BY creation_date DESC";
        List<Profile> profiles = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                profiles.add(mapResultSetToProfile(rs));
            }
        }
        return profiles;
    }

    private Profile mapResultSetToProfile(ResultSet rs) throws SQLException {
        return new Profile(
            rs.getInt("profile_id"),
            rs.getInt("account_id"),
            UUID.fromString(rs.getString("uuid")),
            rs.getString("name"),
            rs.getString("profile_name"),
            rs.getTimestamp("creation_date").toLocalDateTime(),
            rs.getBoolean("is_locked"),
            rs.getInt("playtime_minutes"),
            rs.getTimestamp("last_login").toLocalDateTime(),
            ProfileType.valueOf(rs.getString("profile_type"))
        );
    }

    public void delete(UUID uuid) throws SQLException {
        String sql = "DELETE FROM profiles WHERE uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.executeUpdate();
        }
    }

    public void updatePlaytime(UUID uuid, int additionalMinutes) throws SQLException {
        String sql = """
            UPDATE profiles 
            SET playtime_minutes = playtime_minutes + ?,
                last_login = CURRENT_TIMESTAMP
            WHERE uuid = ?
        """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, additionalMinutes);
            stmt.setString(2, uuid.toString());
            stmt.executeUpdate();
        }
    }
} 