package lunaris.core.error;

import lunaris.core.Lunaris;
import lunaris.core.error.types.ErrorCategory;
import lunaris.core.error.types.ErrorSeverity;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ErrorManager {
    private final Lunaris plugin;
    private final Map<String, GameError> registeredErrors;

    public ErrorManager(Lunaris plugin) {
        this.plugin = plugin;
        this.registeredErrors = new HashMap<>();
        registerDefaultErrors();
    }

    private void registerDefaultErrors() {
        registerError(new GameError(
            "ACC-001",
            ErrorCategory.ACCOUNT,
            "Hesap veritabanından yüklenemedi",
            ErrorSeverity.HIGH
        ));
        registerError(new GameError(
            "ACC-002",
            ErrorCategory.ACCOUNT,
            "Hesap zaten mevcut",
            ErrorSeverity.MEDIUM
        ));

        registerError(new GameError(
            "PRF-001",
            ErrorCategory.PROFILE,
            "Oyuncu profili yüklenemedi",
            ErrorSeverity.HIGH
        ));
        registerError(new GameError(
            "PRF-002",
            ErrorCategory.PROFILE,
            "Geçersiz profil verisi",
            ErrorSeverity.MEDIUM
        ));

        registerError(new GameError(
            "NPC-001",
            ErrorCategory.NPC,
            "NPC oluşturulamadı",
            ErrorSeverity.MEDIUM
        ));
        registerError(new GameError(
            "NPC-002",
            ErrorCategory.NPC,
            "Geçersiz NPC önizleme",
            ErrorSeverity.MEDIUM
        ));

        registerError(new GameError(
            "QST-001",
            ErrorCategory.QUEST,
            "Görev başlatılamadı",
            ErrorSeverity.MEDIUM
        ));
        registerError(new GameError(
            "QST-002",
            ErrorCategory.QUEST,
            "Görev gereksinimleri karşılanmadı",
            ErrorSeverity.LOW
        ));

        registerError(new GameError(
            "ACC-003",
            ErrorCategory.ACCOUNT,
            "Hesap veritabanına kaydedilemedi",
            ErrorSeverity.HIGH
        ));

        registerError(new GameError(
            "PRF-003",
            ErrorCategory.PROFILE,
            "Profil bulunamadı",
            ErrorSeverity.HIGH
        ));
        registerError(new GameError(
            "PRF-004",
            ErrorCategory.PROFILE,
            "Aktif profil bulunamadı",
            ErrorSeverity.HIGH
        ));
        registerError(new GameError(
            "PRF-005",
            ErrorCategory.PROFILE,
            "Profil kaydedilemedi",
            ErrorSeverity.HIGH
        ));

        registerError(new GameError(
            "QST-003",
            ErrorCategory.QUEST,
            "Görevler veritabanından yüklenemedi",
            ErrorSeverity.CRITICAL
        ));
        registerError(new GameError(
            "QST-004",
            ErrorCategory.QUEST,
            "Görev bulunamadı",
            ErrorSeverity.MEDIUM
        ));
    }

    public void registerError(GameError error) {
        registeredErrors.put(error.getErrorId(), error);
    }

    public void handleError(String errorId, Player player, Throwable exception) {
        GameError error = registeredErrors.get(errorId);
        if (error == null) {
            plugin.getLogger().warning("[err->unregistered_handler] " + errorId);
            return;
        }

        if (error.getSeverity() == ErrorSeverity.HIGH || error.getSeverity() == ErrorSeverity.CRITICAL) {
            plugin.getLogger().log(Level.SEVERE, error.getFormattedMessage(), exception);
        } else {
            plugin.getLogger().warning(error.getFormattedMessage());
        }

        if (player != null && player.isOnline()) {
            player.sendMessage(error.getFormattedMessage());
        }

        if (error.getSeverity() == ErrorSeverity.CRITICAL) {
            plugin.getServer().getOnlinePlayers().stream()
                .filter(p -> p.hasPermission("lunaris.admin"))
                .forEach(admin -> admin.sendMessage("§4[KRITIK] " + error.getFormattedMessage()));
        }
    }

    public void handleError(String errorId, Player player) {
        handleError(errorId, player, null);
    }

    public GameError getError(String errorId) {
        return registeredErrors.get(errorId);
    }
} 