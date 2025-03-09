package lunaris.rpg.custom_items;

import lunaris.core.Lunaris;
import lunaris.rpg.classes.PlayerClass;
import lunaris.rpg.custom_items.requirements.RequirementChecker;
import lunaris.rpg.custom_items.souldusts.SoulDust;
import lunaris.rpg.custom_items.stats.ItemStat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public abstract class RPGItem {
    protected final String id;
    protected final String displayName;
    protected final Material material;
    protected final ItemRarity rarity;
    protected final Set<ItemFlag> flags;
    protected final Map<String, String> stats;
    protected final Map<String, Integer> requirements;
    protected final List<String> additionalLore;
    protected PlayerClass requiredClass;
    protected int requiredLevel;
    protected final int soulDustSlots;
    protected final Map<Integer, SoulDust> soulDusts;

    private static final RequirementChecker requirementChecker = new RequirementChecker(Lunaris.getPlugin(Lunaris.class));

    protected RPGItem(Builder<?> builder) {
        this.id = builder.id;
        this.displayName = builder.displayName;
        this.material = builder.material;
        this.rarity = builder.rarity;
        this.flags = builder.flags;
        this.stats = builder.stats;
        this.requirements = builder.requirements;
        this.additionalLore = builder.additionalLore;
        this.requiredClass = builder.requiredClass;
        this.requiredLevel = builder.requiredLevel;
        this.soulDustSlots = calculateSoulDustSlots(builder.rarity);
        this.soulDusts = new HashMap<>();
    }

    private int calculateSoulDustSlots(ItemRarity rarity) {
        return switch (rarity) {
            case NORMAL -> 1;
            case UNIQUE -> 2;
            case RARE -> 2;
            case LEGENDARY -> 3;
            case FABLED -> 3;
            case MYTHIC -> 4;
        };
    }

    public ItemStack createItemStack() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        
        meta.setDisplayName(rarity.getColor() + displayName + " " + rarity.getStars());
        
        
        List<String> lore = new ArrayList<>();
        
        
        stats.forEach((stat, value) -> lore.add("§7" + stat + ": §f" + value));
        
        
        if (!stats.isEmpty()) lore.add("");
        
        
        if (requiredClass != null) {
            lore.add("§7Sınıf: §f" + requiredClass.getFormattedName());
        }
        
        requirements.forEach((req, value) -> {
            lore.add("§7" + req + ": §f" + value);
        });
        
        
        if (!requirements.isEmpty()) lore.add("");
        
        
        additionalLore.forEach(line -> lore.add("§7" + line));
        
        
        if (!additionalLore.isEmpty()) lore.add("");
        
        
        flags.forEach(flag -> lore.add("§4" + flag.getDisplayName()));
        
        
        if (!flags.isEmpty()) lore.add("");
        lore.add(rarity.getColor() + rarity.getDisplayName());
        
        
        lore.add("");
        lore.add("§6Ruh Tozu Yuvaları: §f" + soulDustSlots);
        
        
        soulDusts.forEach((slot, dust) -> {
            lore.add(dust.getType().formatEffect(dust.getLevel()));
        });

        
        for (int i = soulDusts.size(); i < soulDustSlots; i++) {
            lore.add("§7◇ Boş Yuva");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }

    public void give(Player player) {
        player.getInventory().addItem(createItemStack());
    }

    public boolean canUse(Player player) {
        return requirementChecker.meetsRequirements(player, this);
    }

    public PlayerClass getRequiredClass() {
        return requiredClass;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public Map<String, Integer> getRequirements() {
        return Collections.unmodifiableMap(requirements);
    }

    public String getId() {
        return id;
    }

    public static abstract class Builder<T extends Builder<T>> {
        private String id;
        private String displayName;
        private Material material;
        private ItemRarity rarity = ItemRarity.NORMAL;
        private final Set<ItemFlag> flags = new HashSet<>();
        private final Map<String, String> stats = new HashMap<>();
        private final Map<String, Integer> requirements = new HashMap<>();
        private final List<String> additionalLore = new ArrayList<>();
        private PlayerClass requiredClass;
        private int requiredLevel;

        public T id(String id) {
            this.id = id;
            return self();
        }

        public T displayName(String displayName) {
            this.displayName = displayName;
            return self();
        }

        public T material(Material material) {
            this.material = material;
            return self();
        }

        public T rarity(ItemRarity rarity) {
            this.rarity = rarity;
            return self();
        }

        public T addFlag(ItemFlag flag) {
            this.flags.add(flag);
            return self();
        }

        public T addStat(ItemStat stat, String value) {
            this.stats.put(stat.getDisplayName(), value);
            return self();
        }

        public T addRequirement(String requirement, int value) {
            this.requirements.put(requirement, value);
            return self();
        }

        public T addLoreLine(String line) {
            this.additionalLore.add(line);
            return self();
        }

        public T requiredClass(PlayerClass requiredClass) {
            this.requiredClass = requiredClass;
            return self();
        }

        public T requiredLevel(int level) {
            this.requiredLevel = level;
            return self();
        }

        protected abstract T self();
        public abstract RPGItem build();
    }
} 