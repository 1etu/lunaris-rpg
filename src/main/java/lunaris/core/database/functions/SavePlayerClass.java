package lunaris.core.database.functions;

import lunaris.core.Lunaris;
import lunaris.core.account.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SavePlayerClass {
    private final Lunaris plugin;
    private final Connection connection;

    public SavePlayerClass(Lunaris plugin, Connection connection) {
        this.plugin = plugin;
        this.connection = connection;
    }

    public void execute(Account account) throws SQLException {
        String sql = "UPDATE accounts SET player_class = ? WHERE account_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.getPlayerClass().name());
            stmt.setInt(2, account.getAccountId());
            int updated = stmt.executeUpdate();
            plugin.getLogger().info("[dbg->SavePlayerClass] updated " + updated + " rows");
        }
    }
}
