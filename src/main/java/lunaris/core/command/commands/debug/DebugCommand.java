package lunaris.core.command.commands.debug;

import lunaris.core.Lunaris;
import lunaris.core.account.Account;
import lunaris.core.account.AccountModule;
import lunaris.core.command.BaseCommand;
import lunaris.core.command.CommandCategory;
import lunaris.core.command.CommandInfo;
import lunaris.core.rank.Rank;
import lunaris.rpg.profiles.Profile;
import lunaris.rpg.quests.QuestManager;
import lunaris.rpg.quests.QuestObjective;
import lunaris.rpg.npc.NPC;
import lunaris.rpg.npc.NPCManager;
import lunaris.rpg.npc.presets.NPCPreset;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

@CommandInfo(
    name = "debug",
    description = "Debug various systems",
    allowedRanks = {Rank.YONETICI},
    category = CommandCategory.ADMIN
)
public class DebugCommand extends BaseCommand {
    
    public DebugCommand(Lunaris plugin) {
        super(plugin);
    }

    @Override
    protected void execute(Player player, String[] args) {
        if (args.length == 0) {
            sendHelp(player);
            return;
        }

        String category = args[0].toLowerCase();
        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

        // Special handling for NPC commands since they don't need target player
        if (category.equals("npc")) {
            handleNPCDebug(player, newArgs);
            return;
        }
        
        // For all other commands, get target player
        Player target = newArgs.length > 0 ? Bukkit.getPlayer(newArgs[0]) : player;
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        switch (category) {
            case "account":
                debugAccount(player, target);
                break;
            case "profile":
                debugProfile(player, target);
                break;
            case "class":
                debugClass(player, target);
                break;
            case "quest":
                debugQuest(player, target);
                break;
            case "all":
                debugAll(player, target);
                break;
            default:
                sendHelp(player);
        }
    }

    private void debugAccount(Player admin, Player target) {
        plugin.getModuleManager().getModule(AccountModule.class)
            .ifPresent(module -> {
                Account account = module.getAccountService().getOrCreateAccount(target);
                
                admin.sendMessage(ChatColor.GOLD + "=== Account Debug ===");
                admin.sendMessage(ChatColor.GRAY + "UUID: " + ChatColor.WHITE + target.getUniqueId());
                admin.sendMessage(ChatColor.GRAY + "Account ID: " + ChatColor.WHITE + account.getAccountId());
                admin.sendMessage(ChatColor.GRAY + "Name: " + ChatColor.WHITE + account.getName());
                admin.sendMessage(ChatColor.GRAY + "Rank: " + account.getRank().getFormattedName());
                admin.sendMessage(ChatColor.GRAY + "Level: " + ChatColor.WHITE + account.getLevel());
                admin.sendMessage(ChatColor.GRAY + "Class: " + ChatColor.WHITE + account.getPlayerClass().getFormattedName());
                admin.sendMessage(ChatColor.GRAY + "First Join: " + ChatColor.WHITE + account.getFirstLogin());
                admin.sendMessage(ChatColor.GRAY + "Last Login: " + ChatColor.WHITE + account.getLastLogin());
            });
    }

    private void debugProfile(Player admin, Player target) {
        try {
            var profiles = plugin.getProfileRepository().findByAccountId(
                plugin.getModuleManager()
                    .getModule(AccountModule.class)
                    .map(module -> module.getAccountService().getOrCreateAccount(target))
                    .map(Account::getAccountId)
                    .orElse(-1)
            );

            admin.sendMessage(ChatColor.GOLD + "=== Profile Debug ===");
            if (profiles.isEmpty()) {
                admin.sendMessage(ChatColor.RED + "No profiles found!");
                return;
            }

            Profile profile = profiles.get(0);
            admin.sendMessage(ChatColor.GRAY + "Profile ID: " + ChatColor.WHITE + profile.getProfileId());
            admin.sendMessage(ChatColor.GRAY + "Profile Name: " + ChatColor.WHITE + profile.getProfileName());
            admin.sendMessage(ChatColor.GRAY + "Profile Type: " + ChatColor.WHITE + profile.getProfileType().getDisplayName());
            admin.sendMessage(ChatColor.GRAY + "Creation Date: " + ChatColor.WHITE + profile.getCreationDate());
            admin.sendMessage(ChatColor.GRAY + "Playtime: " + ChatColor.WHITE + profile.getFormattedPlaytime());
            admin.sendMessage(ChatColor.GRAY + "Last Login: " + ChatColor.WHITE + profile.getLastLogin());
            admin.sendMessage(ChatColor.GRAY + "Locked: " + ChatColor.WHITE + profile.isLocked());

        } catch (SQLException e) {
            admin.sendMessage(ChatColor.RED + "Error loading profile data: " + e.getMessage());
        }
    }

    private void debugClass(Player admin, Player target) {
        plugin.getModuleManager().getModule(AccountModule.class)
            .ifPresent(module -> {
                Account account = module.getAccountService().getOrCreateAccount(target);
                
                admin.sendMessage(ChatColor.GOLD + "=== Class Debug ===");
                admin.sendMessage(ChatColor.GRAY + "Current Class: " + account.getPlayerClass().getFormattedName());
                admin.sendMessage(ChatColor.GRAY + "Class Name: " + ChatColor.WHITE + account.getPlayerClass().getName());
                admin.sendMessage(ChatColor.GRAY + "Display Name: " + ChatColor.WHITE + account.getPlayerClass().getDisplayName());
                admin.sendMessage(ChatColor.GRAY + "Color: " + ChatColor.WHITE + account.getPlayerClass().getColor().name());
                admin.sendMessage(ChatColor.GRAY + "Icon: " + ChatColor.WHITE + account.getPlayerClass().getIcon());
            });
    }

    private void debugQuest(Player admin, Player target) {
        plugin.getModuleManager().getModule(QuestManager.class)
            .ifPresent(module -> {
                admin.sendMessage(ChatColor.GOLD + "=== Quest Debug ===");
                
                module.getActiveQuest(target).ifPresentOrElse(
                    playerQuest -> {
                        module.getQuest(playerQuest.getQuestId()).ifPresentOrElse(
                            quest -> {
                                admin.sendMessage(ChatColor.GRAY + "Active Quest ID: " + ChatColor.WHITE + quest.getQuestId());
                                admin.sendMessage(ChatColor.GRAY + "Title: " + ChatColor.WHITE + quest.getTitle());
                                admin.sendMessage(ChatColor.GRAY + "Status: " + ChatColor.WHITE + playerQuest.getStatus());
                                admin.sendMessage(ChatColor.GRAY + "Start Time: " + ChatColor.WHITE + playerQuest.getStartTime());
                                
                                admin.sendMessage(ChatColor.GRAY + "Objectives:");
                                for (int i = 0; i < quest.getObjectives().size(); i++) {
                                    QuestObjective objective = quest.getObjectives().get(i);
                                    int progress = playerQuest.getObjectiveProgress(i);
                                    admin.sendMessage(ChatColor.GRAY + "- " + objective.getDescription() + ": " + 
                                        progress + "/" + objective.getRequiredProgress());
                                }
                            },
                            () -> admin.sendMessage(ChatColor.RED + "Quest not found in cache!")
                        );
                    },
                    () -> admin.sendMessage(ChatColor.RED + "No active quest!")
                );
            });
    }

    private void debugAll(Player admin, Player target) {
        admin.sendMessage(ChatColor.GOLD + "========== Debug Info ==========");
        debugAccount(admin, target);
        admin.sendMessage("");
        debugProfile(admin, target);
        admin.sendMessage("");
        debugClass(admin, target);
        admin.sendMessage("");
        debugQuest(admin, target);
        admin.sendMessage(ChatColor.GOLD + "==============================");
    }

    private void handleNPCDebug(Player player, String[] args) {
        if (args.length == 0) {
            sendNPCHelp(player);
            return;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "list":
                listNPCs(player);
                break;
            case "create":
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /debug npc create <preset_id>");
                    return;
                }
                createNPC(player, args[1]);
                break;
            case "delete":
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /debug npc delete <npc_id>");
                    return;
                }
                deleteNPC(player, args[1]);
                break;
            case "presets":
                listPresets(player);
                break;
            default:
                sendNPCHelp(player);
        }
    }

    private void listNPCs(Player player) {
        NPCManager npcManager = plugin.getNPCManager();
        Map<String, NPC> npcs = npcManager.getPresetManager().getLoadedNPCs();
        
        player.sendMessage("§6=== Loaded NPCs ===");
        if (npcs.isEmpty()) {
            player.sendMessage("§7No NPCs loaded.");
            return;
        }

        for (Map.Entry<String, NPC> entry : npcs.entrySet()) {
            NPC npc = entry.getValue();
            Location loc = npc.getLocation();
            player.sendMessage(String.format("§7- §f%s §7(ID: §f%s§7) at §f%s, %s, %s",
                npc.getName(),
                entry.getKey(),
                loc.getBlockX(),
                loc.getBlockY(),
                loc.getBlockZ()
            ));
        }
    }

    private void createNPC(Player player, String presetId) {
        NPCManager npcManager = plugin.getNPCManager();
        Location location = player.getLocation();

        try {
            NPC npc = npcManager.createNPC(presetId, location);
            npcManager.spawnNPC(player, npc);
            player.sendMessage("§aCreated NPC from preset: §f" + presetId);
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cFailed to create NPC: " + e.getMessage());
        }
    }

    private void deleteNPC(Player player, String npcId) {
        NPCManager npcManager = plugin.getNPCManager();
        Map<String, NPC> npcs = npcManager.getPresetManager().getLoadedNPCs();
        
        NPC npc = npcs.get(npcId);
        if (npc == null) {
            player.sendMessage("§cNPC not found with ID: " + npcId);
            return;
        }

        npcManager.despawnNPC(player, npc);
        npcManager.removeNPC(npc.getEntityId());
        player.sendMessage("§aDeleted NPC: §f" + npcId);
    }

    private void listPresets(Player player) {
        NPCManager npcManager = plugin.getNPCManager();
        Map<String, NPCPreset> presets = npcManager.getPresetManager().getPresets();
        
        player.sendMessage("§6=== Available NPC Presets ===");
        if (presets.isEmpty()) {
            player.sendMessage("§7No presets available.");
            return;
        }

        for (String presetId : presets.keySet()) {
            player.sendMessage("§7- §f" + presetId);
        }
    }

    private void sendNPCHelp(Player player) {
        player.sendMessage("§6NPC Debug Commands:");
        player.sendMessage("§7/debug npc list §8- §7List all loaded NPCs");
        player.sendMessage("§7/debug npc create <preset_id> §8- §7Create NPC at your location");
        player.sendMessage("§7/debug npc delete <npc_id> §8- §7Delete an NPC");
        player.sendMessage("§7/debug npc presets §8- §7List available NPC presets");
    }

    private void sendHelp(Player player) {
        player.sendMessage(ChatColor.GOLD + "Debug Commands:");
        player.sendMessage(ChatColor.GRAY + "/debug account [player] " + ChatColor.DARK_GRAY + "- " + 
            ChatColor.GRAY + "Show account info");
        player.sendMessage(ChatColor.GRAY + "/debug profile [player] " + ChatColor.DARK_GRAY + "- " + 
            ChatColor.GRAY + "Show profile info");
        player.sendMessage(ChatColor.GRAY + "/debug class [player] " + ChatColor.DARK_GRAY + "- " + 
            ChatColor.GRAY + "Show class info");
        player.sendMessage(ChatColor.GRAY + "/debug quest [player] " + ChatColor.DARK_GRAY + "- " + 
            ChatColor.GRAY + "Show quest info");
        player.sendMessage(ChatColor.GRAY + "/debug all [player] " + ChatColor.DARK_GRAY + "- " + 
            ChatColor.GRAY + "Show all debug info");
        player.sendMessage(ChatColor.GRAY + "/debug npc §8- §7NPC management commands");
    }
} 