package lunaris.core.database;

import lunaris.core.Lunaris;
import lunaris.core.account.Account;
import lunaris.core.rank.Rank;
import lunaris.rpg.classes.PlayerClass;
import lunaris.core.database.migrations.AddAccountIdMigration;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class AccountRepository extends Repository {

    public AccountRepository(Lunaris plugin) {
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
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                CREATE TABLE IF NOT EXISTS accounts (
                    account_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    uuid VARCHAR(36) NOT NULL UNIQUE,
                    name VARCHAR(16) NOT NULL,
                    rank VARCHAR(32) NOT NULL,
                    level INT NOT NULL,
                    xp INT NOT NULL DEFAULT 0,
                    first_login TIMESTAMP NOT NULL,
                    last_login TIMESTAMP NOT NULL,
                    player_class VARCHAR(32) NOT NULL DEFAULT 'NONE',
                    combat_level INT NOT NULL DEFAULT 1,
                    woodcutting_level INT NOT NULL DEFAULT 1,
                    mining_level INT NOT NULL DEFAULT 1,
                    farming_level INT NOT NULL DEFAULT 1,
                    fishing_level INT NOT NULL DEFAULT 1,
                    cooking_level INT NOT NULL DEFAULT 1,
                    alchemism_level INT NOT NULL DEFAULT 1,
                    armouring_level INT NOT NULL DEFAULT 1,
                    weaponsmithing_level INT NOT NULL DEFAULT 1,
                    woodworking_level INT NOT NULL DEFAULT 1,
                    health INT NOT NULL DEFAULT 100,
                    defense INT NOT NULL DEFAULT 10,
                    intelligence INT NOT NULL DEFAULT 10
                )
            """);
            
            AddAccountIdMigration.migrate(connection);
        }
    }

    public void save(Account account) throws SQLException {
        plugin.getLogger().info("[dbg->AccountRepository] save acc: " + account.getPlayerClass());
        
        String updateSql = """
            UPDATE accounts 
            SET name = ?, rank = ?, level = ?, xp = ?, last_login = ?,
                player_class = ?, combat_level = ?, woodcutting_level = ?, mining_level = ?,
                farming_level = ?, fishing_level = ?, cooking_level = ?, alchemism_level = ?,
                armouring_level = ?, weaponsmithing_level = ?, woodworking_level = ?,
                health = ?, defense = ?, intelligence = ?
            WHERE uuid = ?
        """;
        
        try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
            updateStmt.setString(1, account.getName());
            updateStmt.setString(2, account.getRank().name());
            updateStmt.setInt(3, account.getLevel());
            updateStmt.setInt(4, account.getXP());
            updateStmt.setTimestamp(5, Timestamp.valueOf(account.getLastLogin()));
            updateStmt.setString(6, account.getPlayerClass().name());
            updateStmt.setInt(7, account.getCombatLevel());
            updateStmt.setInt(8, account.getWoodcuttingLevel());
            updateStmt.setInt(9, account.getMiningLevel());
            updateStmt.setInt(10, account.getFarmingLevel());
            updateStmt.setInt(11, account.getFishingLevel());
            updateStmt.setInt(12, account.getCookingLevel());
            updateStmt.setInt(13, account.getAlchemismLevel());
            updateStmt.setInt(14, account.getArmouringLevel());
            updateStmt.setInt(15, account.getWeaponsmithingLevel());
            updateStmt.setInt(16, account.getWoodworkingLevel());
            updateStmt.setInt(17, account.getHealth());
            updateStmt.setInt(18, account.getDefense());
            updateStmt.setInt(19, account.getIntelligence());
            updateStmt.setString(20, account.getUuid().toString());
            
            int updated = updateStmt.executeUpdate();
            plugin.getLogger().info("[dbg->AccountRepository] updated " + updated + " rows");
            
            if (updated == 0) {
                String insertSql = """
                    INSERT INTO accounts (uuid, name, rank, level, xp, first_login, last_login,
                        player_class, combat_level, woodcutting_level, mining_level, farming_level,
                        fishing_level, cooking_level, alchemism_level, armouring_level,
                        weaponsmithing_level, woodworking_level, health, defense, intelligence)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
                
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setString(1, account.getUuid().toString());
                    insertStmt.setString(2, account.getName());
                    insertStmt.setString(3, account.getRank().name());
                    insertStmt.setInt(4, account.getLevel());
                    insertStmt.setInt(5, account.getXP());
                    insertStmt.setTimestamp(6, Timestamp.valueOf(account.getFirstLogin()));
                    insertStmt.setTimestamp(7, Timestamp.valueOf(account.getLastLogin()));
                    insertStmt.setString(8, account.getPlayerClass().name());
                    insertStmt.setInt(9, account.getCombatLevel());
                    insertStmt.setInt(10, account.getWoodcuttingLevel());
                    insertStmt.setInt(11, account.getMiningLevel());
                    insertStmt.setInt(12, account.getFarmingLevel());
                    insertStmt.setInt(13, account.getFishingLevel());
                    insertStmt.setInt(14, account.getCookingLevel());
                    insertStmt.setInt(15, account.getAlchemismLevel());
                    insertStmt.setInt(16, account.getArmouringLevel());
                    insertStmt.setInt(17, account.getWeaponsmithingLevel());
                    insertStmt.setInt(18, account.getWoodworkingLevel());
                    insertStmt.setInt(19, account.getHealth());
                    insertStmt.setInt(20, account.getDefense());
                    insertStmt.setInt(21, account.getIntelligence());
                    insertStmt.executeUpdate();
                }
            }
        }
    }

    public Optional<Account> findByUUID(UUID uuid) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE uuid = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(loadAccount(rs));
            }
        }
        return Optional.empty();
    }

    private Account loadAccount(ResultSet resultSet) throws SQLException {
        return new Account(
            resultSet.getInt("account_id"),
            UUID.fromString(resultSet.getString("uuid")),
            resultSet.getString("name"),
            Rank.valueOf(resultSet.getString("rank")),
            resultSet.getInt("level"),
            resultSet.getInt("xp"),
            resultSet.getTimestamp("first_login").toLocalDateTime(),
            resultSet.getTimestamp("last_login").toLocalDateTime(),
            PlayerClass.valueOf(resultSet.getString("player_class")),
            resultSet.getInt("combat_level"),
            resultSet.getInt("woodcutting_level"),
            resultSet.getInt("mining_level"),
            resultSet.getInt("farming_level"),
            resultSet.getInt("fishing_level"),
            resultSet.getInt("cooking_level"),
            resultSet.getInt("alchemism_level"),
            resultSet.getInt("armouring_level"),
            resultSet.getInt("weaponsmithing_level"),
            resultSet.getInt("woodworking_level"),
            resultSet.getInt("health"),
            resultSet.getInt("defense"),
            resultSet.getInt("intelligence"),
            plugin
        );
    }

    public void delete(UUID uuid) throws SQLException {
        String sql = "DELETE FROM accounts WHERE uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.executeUpdate();
        }
    }

    public interface SQLFunction<T> {
        T apply(PreparedStatement stmt) throws SQLException;
    }

    public int executeUpdate(String sql, SQLFunction<Integer> statementConsumer) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            return statementConsumer.apply(stmt);
        }
    }
} 