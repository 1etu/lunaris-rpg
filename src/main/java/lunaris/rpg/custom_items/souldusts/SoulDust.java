package lunaris.rpg.custom_items.souldusts;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class SoulDust {
    private final SoulDustType type;
    private final int level;

    public SoulDust(SoulDustType type, int level) {
        this.type = type;
        this.level = level;
    }

    public SoulDustType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public ItemStack createItemStack() {
        ItemStack item = new ItemStack(Material.GLOWSTONE_DUST);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(type.getDisplayName() + " Ruh Tozu §7(Seviye " + level + ")");
        
        List<String> lore = new ArrayList<>();
        lore.add(type.formatEffect(level));
        lore.add("");
        lore.add("§7Bir eşyanın Ruh Tozu Yuvasına");
        lore.add("§7yerleştirmek için Ruh Ocağı'nı");
        lore.add("§7ziyaret et.");
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
} 