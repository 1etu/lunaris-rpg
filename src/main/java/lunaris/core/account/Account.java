package lunaris.core.account;

import lunaris.core.account.api.IAccount;
import lunaris.core.rank.Rank;
import lunaris.rpg.classes.PlayerClass;
import lunaris.core.Lunaris;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.time.LocalDateTime;
import java.util.UUID;

public class Account implements IAccount {
    private final int accountId;
    private final UUID uuid;
    private String name;
    private Rank rank;
    private int level;
    private int xp;
    private final LocalDateTime firstLogin;
    private LocalDateTime lastLogin;
    private PlayerClass playerClass = PlayerClass.NONE;
    private int combatLevel;
    private int woodcuttingLevel;
    private int miningLevel;
    private int farmingLevel;
    private int fishingLevel;
    private int cookingLevel;
    private int alchemismLevel;
    private int armouringLevel;
    private int weaponsmithingLevel;
    private int woodworkingLevel;
    private int health = 100;
    private int defense;
    private int intelligence;
    private final Lunaris plugin;

    public Account(int accountId, UUID uuid, String name, Rank rank, int level, int xp, 
        LocalDateTime firstLogin, LocalDateTime lastLogin, PlayerClass playerClass,
        int combatLevel, int woodcuttingLevel, int miningLevel, int farmingLevel,
        int fishingLevel, int cookingLevel, int alchemismLevel, int armouringLevel,
        int weaponsmithingLevel, int woodworkingLevel, int health, int defense, int intelligence,
        Lunaris plugin) {
        
        this.accountId = accountId;
        this.uuid = uuid;
        this.name = name;
        this.rank = rank;
        this.level = level;
        this.xp = xp;
        this.firstLogin = firstLogin;
        this.lastLogin = lastLogin;
        this.playerClass = playerClass;
        this.combatLevel = combatLevel;
        this.woodcuttingLevel = woodcuttingLevel;
        this.miningLevel = miningLevel;
        this.farmingLevel = farmingLevel;
        this.fishingLevel = fishingLevel;
        this.cookingLevel = cookingLevel;
        this.alchemismLevel = alchemismLevel;
        this.armouringLevel = armouringLevel;
        this.weaponsmithingLevel = weaponsmithingLevel;
        this.woodworkingLevel = woodworkingLevel;
        this.health = health;
        this.defense = defense;
        this.intelligence = intelligence;
        this.plugin = plugin;
    }

    public static Account createDefault(Player player, Lunaris plugin) {
        return new Account(
            -1, // db AUTO_INCREMENT, please leave this -1
            player.getUniqueId(),
            player.getName(),
            Rank.OYUNCU,
            1,
            0,
            LocalDateTime.now(),
            LocalDateTime.now(),
            PlayerClass.NONE,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            100,
            1,
            1,
            plugin
        );
    }

    public ChatColor getLevelColor() {
        if (level >= 180) return ChatColor.DARK_RED;
        if (level >= 120) return ChatColor.DARK_PURPLE;
        if (level >= 90) return ChatColor.GOLD;
        if (level >= 60) return ChatColor.RED;
        if (level >= 30) return ChatColor.LIGHT_PURPLE;
        return ChatColor.GRAY;
    }

    public String formatChatMessage(String message) {
        String prefix = rank.getPrefix();
        String prefixPart = (prefix != null && !prefix.isEmpty()) ? prefix + " " : "";
        String classPart = playerClass != PlayerClass.NONE ? playerClass.getIcon() + " " : "";
        
        return ChatColor.translateAlternateColorCodes('&',
                getLevelColor() + "[" + level + "] " + 
                classPart +
                prefixPart +
                rank.getColor() + name +
                ChatColor.WHITE + ": " +
                ChatColor.WHITE + message);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXP() {
        return xp;
    }

    public void setXP(int xp) {
        this.xp = xp;
    }

    public void addXP(int amount) {
        int newXP = this.xp + amount;
        while (newXP >= 100) {
            newXP -= 100;
            this.level++;
        }
        this.xp = newXP;
    }

    public LocalDateTime getFirstLogin() {
        return firstLogin;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public void setPlayerClass(PlayerClass playerClass) {
        plugin.getLogger().info("[Debug] Setting player class to: " + (playerClass != null ? playerClass.name() : "null"));
        this.playerClass = playerClass != null ? playerClass : PlayerClass.NONE;
        plugin.getLogger().info("[Debug] Player class is now: " + this.playerClass.name());
    }

    public int getCombatLevel() { return combatLevel; }
    public void setCombatLevel(int level) { this.combatLevel = level; }

    public int getWoodcuttingLevel() { return woodcuttingLevel; }
    public void setWoodcuttingLevel(int level) { this.woodcuttingLevel = level; }

    public int getMiningLevel() { return miningLevel; }
    public void setMiningLevel(int level) { this.miningLevel = level; }

    public int getFarmingLevel() { return farmingLevel; }
    public void setFarmingLevel(int level) { this.farmingLevel = level; }

    public int getFishingLevel() { return fishingLevel; }
    public void setFishingLevel(int level) { this.fishingLevel = level; }

    public int getCookingLevel() { return cookingLevel; }
    public void setCookingLevel(int level) { this.cookingLevel = level; }

    public int getAlchemismLevel() { return alchemismLevel; }
    public void setAlchemismLevel(int level) { this.alchemismLevel = level; }

    public int getArmouringLevel() { return armouringLevel; }
    public void setArmouringLevel(int level) { this.armouringLevel = level; }

    public int getWeaponsmithingLevel() { return weaponsmithingLevel; }
    public void setWeaponsmithingLevel(int level) { this.weaponsmithingLevel = level; }

    public int getWoodworkingLevel() { return woodworkingLevel; }
    public void setWoodworkingLevel(int level) { this.woodworkingLevel = level; }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }

    public int getIntelligence() { return intelligence; }
    public void setIntelligence(int intelligence) { this.intelligence = intelligence; }

    public int getAccountId() {
        return accountId;
    }
} 