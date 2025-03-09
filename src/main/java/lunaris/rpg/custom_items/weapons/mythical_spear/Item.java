package lunaris.rpg.custom_items.weapons.mythical_spear;

import lunaris.rpg.classes.PlayerClass;
import lunaris.rpg.custom_items.ItemRarity;
import lunaris.rpg.custom_items.weapons.RPGWeapon;
import lunaris.rpg.custom_items.stats.ItemStat;
import org.bukkit.Material;

public class Item {
    private static final RPGWeapon INSTANCE = new RPGWeapon.Builder()
        .id("mythical_spear")
        .displayName("Mızrak")
        .material(Material.TRIDENT)
        .rarity(ItemRarity.LEGENDARY)
        .damage(40, 60)
        .attackSpeed(1.4)
        .requiredClass(PlayerClass.WARRIOR)
        .addRequirement("Savaş Seviyesi", 35)
        .addRequirement("Güç", 15)
        .addRequirement("Zeka", 15)
        .addStat(ItemStat.EARTH_DAMAGE, "20-100")
        .addStat(ItemStat.WATER_DAMAGE, "40-80")
        .addStat(ItemStat.STRENGTH, "+10")
        .addStat(ItemStat.MAIN_ATTACK, "+3% ile +14%")
        .addStat(ItemStat.MOVEMENT_SPEED, "-19% ile -10%")
        .addStat(ItemStat.WATER_DAMAGE, "+4% ile +16%")
        .addStat(ItemStat.AIR_DEFENSE, "-25% ile -13%")
        .addStat(ItemStat.SOUL_REND, "+15%")
        .addStat(ItemStat.PIERCING_STRIKE, "+25%")
        .addStat(ItemStat.CHAOS_DAMAGE, "20-100")
        .build();

    public static RPGWeapon get() {
        return INSTANCE;
    }
} 