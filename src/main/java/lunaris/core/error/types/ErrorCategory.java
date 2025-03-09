package lunaris.core.error.types;

public enum ErrorCategory {
    ACCOUNT("ACC"),
    PROFILE("PRF"),
    NPC("NPC"),
    QUEST("QST"),
    INVENTORY("INV"),
    DATABASE("DB"),
    PERMISSION("PRM"),
    NETWORK("NET"),
    SYSTEM("SYS");

    private final String prefix;

    ErrorCategory(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
} 