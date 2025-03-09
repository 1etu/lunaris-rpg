package lunaris.core.command;

public enum CommandCategory {
    GENERAL("Genel", "§7"),
    GAME("Oyun", "§a"),
    ADMIN("Yönetim", "§c"),
    MODERATION("Moderasyon", "§b");

    private final String name;
    private final String color;

    CommandCategory(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
} 