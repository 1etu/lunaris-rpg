package lunaris.rpg.custom_items.armors.dragon_plate;

import lunaris.rpg.classes.PlayerClass;
import lunaris.rpg.custom_items.ItemRarity;
import lunaris.rpg.custom_items.armors.RPGArmor;
import lunaris.rpg.custom_items.armors.ArmorType;
import org.bukkit.Material;

public class Item {
    private static final RPGArmor INSTANCE = new RPGArmor.Builder()
        .id("dragon_plate")
        .displayName("Ejder Zırhı")
        .material(Material.NETHERITE_CHESTPLATE)
        .rarity(ItemRarity.MYTHIC)
        .armorType(ArmorType.CHESTPLATE)
        .defense(250)
        .magicDefense(150)
        .requiredClass(PlayerClass.WARRIOR)
        .addRequirement("Savaş Seviyesi", 50)
        .addRequirement("Güç", 25)
        .addLoreLine("Ejderhaların Pullarından Yapılmış")
        .addLoreLine("[3] Toz Yuvası")
        .build();

    public static RPGArmor get() {
        return INSTANCE;
    }
} 