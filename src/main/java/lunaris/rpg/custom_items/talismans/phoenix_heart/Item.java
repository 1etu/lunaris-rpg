package lunaris.rpg.custom_items.talismans.phoenix_heart;

import lunaris.rpg.custom_items.ItemFlag;
import lunaris.rpg.custom_items.ItemRarity;
import lunaris.rpg.custom_items.talismans.RPGTalisman;
import lunaris.rpg.custom_items.talismans.TalismanEffect;
import lunaris.rpg.custom_items.stats.ItemStat;
import org.bukkit.Material;

public class Item {
    private static final RPGTalisman INSTANCE = new RPGTalisman.Builder()
        .id("phoenix_heart")
        .displayName("Anka Kuşu Kalbi")
        .material(Material.HEART_OF_THE_SEA)
        .rarity(ItemRarity.FABLED)
        .effect(TalismanEffect.HEALTH_BOOST)
        .power(5)
        .addFlag(ItemFlag.SOULBOUND)
        .addRequirement("Seviye", 40)
        .addStat(ItemStat.HEALTH_REGEN, "+25%")
        .addStat(ItemStat.MAX_HEALTH, "+150")
        .addLoreLine("Ölümden Sonra Dirilme Şansı: %5")
        .build();

    public static RPGTalisman get() {
        return INSTANCE;
    }
} 