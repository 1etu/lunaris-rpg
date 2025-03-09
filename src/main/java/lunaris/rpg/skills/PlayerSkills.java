package lunaris.rpg.skills;

import lunaris.core.account.Account;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;

public class PlayerSkills {
    private final Account account;
    private final Map<SkillType, Skill> skills;

    public PlayerSkills(Account account) {
        this.account = account;
        this.skills = new EnumMap<>(SkillType.class);
        
        for (SkillType type : SkillType.values()) {
            skills.put(type, new Skill(type));
        }
        
        loadSkillLevels();
    }

    private void loadSkillLevels() {
        skills.get(SkillType.COMBAT).setLevel(account.getCombatLevel());
        skills.get(SkillType.WOODCUTTING).setLevel(account.getWoodcuttingLevel());
        skills.get(SkillType.MINING).setLevel(account.getMiningLevel());
        skills.get(SkillType.FARMING).setLevel(account.getFarmingLevel());
        skills.get(SkillType.FISHING).setLevel(account.getFishingLevel());
        skills.get(SkillType.COOKING).setLevel(account.getCookingLevel());
        skills.get(SkillType.ALCHEMISM).setLevel(account.getAlchemismLevel());
        skills.get(SkillType.ARMOURING).setLevel(account.getArmouringLevel());
        skills.get(SkillType.WEAPONSMITHING).setLevel(account.getWeaponsmithingLevel());
    }

    public void saveSkillLevels() {
        // Comment<1etu@icloud.com>: Potential race condition if skills are modified during save.
        account.setCombatLevel(skills.get(SkillType.COMBAT).getLevel());
        account.setWoodcuttingLevel(skills.get(SkillType.WOODCUTTING).getLevel());
        account.setMiningLevel(skills.get(SkillType.MINING).getLevel());
        account.setFarmingLevel(skills.get(SkillType.FARMING).getLevel());
        account.setFishingLevel(skills.get(SkillType.FISHING).getLevel());
        account.setCookingLevel(skills.get(SkillType.COOKING).getLevel());
        account.setAlchemismLevel(skills.get(SkillType.ALCHEMISM).getLevel());
        account.setArmouringLevel(skills.get(SkillType.ARMOURING).getLevel());
        account.setWeaponsmithingLevel(skills.get(SkillType.WEAPONSMITHING).getLevel());
    }

    public Skill getSkill(SkillType type) {
        return skills.get(type);
    }

    public Map<SkillType, Skill> getAllSkills() {
        return skills;
    }

    public void addXp(SkillType type, int amount) {
        Skill skill = skills.get(type);
        int oldLevel = skill.getLevel();
        skill.addXp(amount);
        
        Player player = account.getPlayer();
        if (player != null && player.isOnline()) {
            player.sendMessage(String.format(
                "§3+%d %s XP §7(%d/100)",
                amount,
                type.getDisplayName(),
                skill.getXp()
            ));
            
            // If level up occurred
            if (skill.getLevel() > oldLevel) {
                player.sendMessage(String.format(
                    "§6⭐ §e%s level up! §7(%d → %d)",
                    type.getDisplayName(),
                    oldLevel,
                    skill.getLevel()
                ));
            }
        }
        
        saveSkillLevels();
    }
} 