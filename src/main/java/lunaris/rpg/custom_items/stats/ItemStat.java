package lunaris.rpg.custom_items.stats;

import net.md_5.bungee.api.ChatColor;

public enum ItemStat {
    SOUL_REND("Ruh Parçalama", "Ekstra hasar verme şansı", StatType.ATTACK),
    PIERCING_STRIKE("Delici Vuruş", "Zırhı yok sayma şansı", StatType.ATTACK),
    CHAOS_DAMAGE("Kaos Hasarı", "Rastgele hasar artışı", StatType.ATTACK),
    DOOM_STRIKE("Kıyamet Darbesi", "Kritik hasar şansı", StatType.ATTACK),
    HEMORRHAGE("Kan Akışı", "Kanama hasarı şansı", StatType.ATTACK),

    ETHEREAL_SHIELD("Eter Koruması", "Büyü direnci", StatType.DEFENSE),
    THORNMAIL("Dikenli Zırh", "Hasar yansıtma", StatType.DEFENSE),
    SHADOW_RESISTANCE("Gölge Direnci", "Karanlık direnci", StatType.DEFENSE),
    LUMIN_BARRIER("Işık Bariyeri", "Büyü hasarı azaltma", StatType.DEFENSE),
    UNYIELDING_WILL("Yıkılmaz İrade", "Kontrol direnci", StatType.DEFENSE),
    DEFENSE("Savunma", "Fiziksel savunma", StatType.DEFENSE),
    MAGIC_DEFENSE("Büyü Savunması", "Büyü savunması", StatType.DEFENSE),

    SHADOWSTEP("Gölge Adım", "Hareket hızı artışı", StatType.SPEED),
    ZEPHYR_GLIDE("Rüzgar Esintisi", "Düşüş hasarı azaltma", StatType.SPEED),
    MASTER_REFLEX("Refleks Ustası", "Kaçınma şansı", StatType.SPEED),
    PHANTOM_VELOCITY("Hayalet Hız", "Saldırı hızı artışı", StatType.SPEED),
    TIME_BREAK("Zamana Meydan Okuma", "Eylem hızı artışı", StatType.SPEED),

    TALISMAN_EFFECT("Etki", "Tılsım etkisi", StatType.SPECIAL),
    TALISMAN_POWER("Güç", "Tılsım gücü", StatType.SPECIAL),

    HEALTH_REGEN("Can Yenilenmesi", "Can yenileme hızı", StatType.SPECIAL),
    MAX_HEALTH("Maksimum Can", "Maksimum can değeri", StatType.SPECIAL),

    DAMAGE("Hasar", "Silah hasarı", StatType.ATTACK),
    ATTACK_SPEED("Saldırı Hızı", "Saldırı hızı", StatType.ATTACK),
    DPS("DPS", "Saniye başına hasar", StatType.ATTACK),

    EARTH_DAMAGE("Toprak Hasarı", "Toprak elemental hasarı", StatType.ATTACK),
    WATER_DAMAGE("Su Hasarı", "Su elemental hasarı", StatType.ATTACK),
    STRENGTH("Güç", "Fiziksel güç artışı", StatType.ATTACK),

    MAIN_ATTACK("Ana Saldırı Hasarı", "Ana saldırı güç artışı", StatType.ATTACK),
    MOVEMENT_SPEED("Yürüme Hızı", "Hareket hızı değişimi", StatType.SPEED),
    AIR_DEFENSE("Hava Savunması", "Hava elementine karşı direnç", StatType.DEFENSE);

    private final String displayName;
    private final String description;
    private final StatType type;

    ItemStat(String displayName, String description, StatType type) {
        this.displayName = displayName;
        this.description = description;
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public StatType getType() {
        return type;
    }

    public String format(String value) {
        return "§7" + displayName + ": §f" + value;
    }

    public enum StatType {
        ATTACK(ChatColor.RED),
        DEFENSE(ChatColor.BLUE),
        SPEED(ChatColor.GREEN),
        SPECIAL(ChatColor.YELLOW);

        private final ChatColor color;

        StatType(ChatColor color) {
            this.color = color;
        }

        public ChatColor getColor() {
            return color;
        }
    }
} 