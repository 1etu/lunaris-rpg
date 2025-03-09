package lunaris.rpg.skills;

import org.bukkit.ChatColor;

public class Skill {
    private final SkillType type;
    private int level;
    private int xp;
    private static final int XP_PER_LEVEL = 100;

    public Skill(SkillType type) {
        this.type = type;
        this.level = 1;
        this.xp = 0;
    }

    public SkillType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(1, Math.min(200, level));
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = Math.max(0, xp);
    }

    public void addXp(int amount) {
        this.xp += amount;
        while (this.xp >= XP_PER_LEVEL) {
            this.xp -= XP_PER_LEVEL;
            this.level++;
        }
    }

    public ChatColor getLevelColor() {
        if (level >= 180) return ChatColor.DARK_RED;
        if (level >= 120) return ChatColor.DARK_PURPLE;
        if (level >= 90) return ChatColor.GOLD;
        if (level >= 60) return ChatColor.RED;
        if (level >= 30) return ChatColor.LIGHT_PURPLE;
        return ChatColor.GRAY;
    }

    public String getProgressBar() {
        int filledBars = (int) ((xp / (double) XP_PER_LEVEL) * 10);
        StringBuilder bar = new StringBuilder();
        
        for (int i = 0; i < 10; i++) {
            if (i < filledBars) {
                bar.append(ChatColor.GREEN).append("■");
            } else {
                bar.append(ChatColor.GRAY).append("■");
            }
        }
        
        return bar.toString();
    }
} 