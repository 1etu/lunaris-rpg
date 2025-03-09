package lunaris.rpg.profiles;

public enum ProfileType {
    TEKIL("Tekil", "§7Tek kişilik profil"),
    PAYLASIMLI("Paylaşımlı", "§7Arkadaşlarınla paylaşabileceğin profil"),
    DEMIR("Demir", "§7Zorlu bir deneyim için demir profil");

    private final String displayName;
    private final String description;

    ProfileType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
} 