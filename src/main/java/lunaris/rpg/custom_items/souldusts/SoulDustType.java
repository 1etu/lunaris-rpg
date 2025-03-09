package lunaris.rpg.custom_items.souldusts;

import java.util.Random;

import net.md_5.bungee.api.ChatColor;

public enum SoulDustType {
    BLOOD("Kan", ChatColor.RED, "Can Çalma", "+{level}% Can Çalma"),
    FLAME("Alev", ChatColor.GOLD, "Yakıcı Güç", "+{level}% Yanan Hasar"),
    ICE("Buz", ChatColor.AQUA, "Dondurucu", "+{level}% Yavaşlatma Şansı"),
    SHADOW("Gölge", ChatColor.DARK_PURPLE, "Gizlilik", "+{level}% Gizlilik Süresi"),
    STORM("Fırtına", ChatColor.YELLOW, "Yıldırım", "+{level}% Kritik Vuruş");

    private final String displayName;
    private final ChatColor color;
    private final String effectName;
    private final String effectFormat;

    SoulDustType(String displayName, ChatColor color, String effectName, String effectFormat) {
        this.displayName = displayName;
        this.color = color;
        this.effectName = effectName;
        this.effectFormat = effectFormat;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String formatEffect(int level) {
        return color + effectFormat.replace("{level}", String.valueOf(getEffectValue(level)));
    }

    private int getEffectValue(int level) {
        return switch (level) {
            case 1 -> 5;
            case 2 -> 10;
            case 3 -> 15;
            default -> 0;
        };
    }

    public static SoulDustType getRandomType() {
        return values()[new Random().nextInt(values().length)];
    }
} 