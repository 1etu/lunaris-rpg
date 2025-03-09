package lunaris.core.game.listeners;

import lunaris.core.Lunaris;
import lunaris.core.account.Account;
import lunaris.core.account.AccountModule;
import lunaris.core.module.ListenerModule;
import lunaris.core.module.ModuleInfo;
import lunaris.rpg.skills.SkillType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener extends ListenerModule {

    public ChatListener(Lunaris plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        plugin.getModuleManager().getModule(AccountModule.class).ifPresent(accountModule -> {
            Account account = accountModule.getAccountService().getOrCreateAccount(event.getPlayer());
            TextComponent message = new TextComponent(account.formatChatMessage(event.getMessage()));
            
            StringBuilder hoverText = new StringBuilder();
            
            hoverText.append(account.getRank().getColor())
                    .append(account.getRank().getFormattedName())
                    .append(" ")
                    .append(account.getName())
                    .append("\n\n");
            
            hoverText.append(ChatColor.GRAY).append("Uzmanlık: ")
                    .append(ChatColor.WHITE)
                    .append(account.getPlayerClass().getDisplayName())
                    .append("\n");
            
            hoverText.append(ChatColor.GRAY).append("Savaşçı Seviyesi: ")
                    .append(ChatColor.WHITE)
                    .append(account.getCombatLevel())
                    .append("\n\n");
            
            SkillType[] gatheringSkills = {
                SkillType.WOODCUTTING,
                SkillType.MINING,
                SkillType.FARMING,
                SkillType.FISHING
            };
            
            for (SkillType skill : gatheringSkills) {
                appendSkill(hoverText, account, skill);
            }
            
            hoverText.append("\n\n");
            
            for (SkillType skill : SkillType.values()) {
                if (!isGatheringSkill(skill)) {
                    appendSkill(hoverText, account, skill);
                }
            }
            
            hoverText.append("\n\n")
                    .append(ChatColor.GOLD).append("Seviye: ")
                    .append(ChatColor.YELLOW).append(account.getLevel());
            
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
                new ComponentBuilder(hoverText.toString()).create()));
            
            event.setCancelled(true);
            event.getPlayer().getServer().spigot().broadcast(message);
        });
    }
    
    private void appendSkill(StringBuilder builder, Account account, SkillType skill) {
        builder.append(skill.getIcon()).append(" ")
               .append(ChatColor.GRAY).append(skill.getDisplayName()).append(": ")
               .append(ChatColor.WHITE);
               
        switch(skill) {
            case WOODCUTTING -> builder.append(account.getWoodcuttingLevel());
            case MINING -> builder.append(account.getMiningLevel());
            case FARMING -> builder.append(account.getFarmingLevel());
            case FISHING -> builder.append(account.getFishingLevel());
            case COOKING -> builder.append(account.getCookingLevel());
            case ALCHEMISM -> builder.append(account.getAlchemismLevel());
            case ARMOURING -> builder.append(account.getArmouringLevel());
            case WEAPONSMITHING -> builder.append(account.getWeaponsmithingLevel());
            case COMBAT -> builder.append(account.getCombatLevel());
        }
        builder.append("\n");
    }
    
    private boolean isGatheringSkill(SkillType skill) {
        return skill == SkillType.WOODCUTTING ||
               skill == SkillType.MINING ||
               skill == SkillType.FARMING ||
               skill == SkillType.FISHING;
    }

    @Override
    public String getName() {
        return "ChatListener";
    }

    @Override
    public ModuleInfo getInfo() {
        return null;
    }
} 