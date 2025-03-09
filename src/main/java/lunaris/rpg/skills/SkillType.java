package lunaris.rpg.skills;

public enum SkillType {
    COMBAT("Savaş", "⚔"),
    WOODCUTTING("Odunculuk", "🪓"),
    MINING("Madencilik", "⛏"),
    FARMING("Tarımcılık", "🌾"),
    FISHING("Balıkçılık", "🎣"),
    COOKING("Pişirme", "🍖"),
    ALCHEMISM("Simyacılık", "⚗"),
    ARMOURING("Zırhçılık", "🛡"),
    WEAPONSMITHING("Silahçılık", "⚒");

    private final String displayName;
    private final String icon;

    SkillType(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }
} 