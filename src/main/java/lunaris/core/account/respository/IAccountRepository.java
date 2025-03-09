package lunaris.core.account.respository;

import lunaris.core.account.Account;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public interface IAccountRepository {
    void save(Account account) throws SQLException;
    Optional<Account> findByUUID(UUID uuid) throws SQLException;
    void delete(UUID uuid) throws SQLException;
    boolean exists(UUID uuid) throws SQLException;
} 