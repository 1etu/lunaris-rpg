package lunaris.core.error;

import lunaris.core.error.types.ErrorCategory;
import lunaris.core.error.types.ErrorSeverity;

public class GameError {
    private final String errorId;
    private final ErrorCategory category;
    private final String message;
    private final ErrorSeverity severity;

    public GameError(String errorId, ErrorCategory category, String message, ErrorSeverity severity) {
        this.errorId = errorId;
        this.category = category;
        this.message = message;
        this.severity = severity;
    }

    public String getErrorId() {
        return errorId;
    }

    public ErrorCategory getCategory() {
        return category;
    }

    public String getMessage() {
        return message;
    }

    public ErrorSeverity getSeverity() {
        return severity;
    }

    public String getFormattedMessage() {
        return String.format("§c[%s] §7%s §8(Hata ID: %s)", 
            category.name(), 
            message, 
            errorId
        );
    }
} 