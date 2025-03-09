package lunaris.rpg.quests;

import lunaris.core.Lunaris;
import lunaris.core.account.Account;
import lunaris.core.account.AccountModule;
import lunaris.core.module.IModule;
import lunaris.core.module.ModuleInfo;
import lunaris.rpg.classes.PlayerClass;
import lunaris.rpg.profiles.events.PlayerProfileCreateEvent;
import lunaris.rpg.quests.objectives.TalkToNPCObjective;
import lunaris.rpg.scoreboard.ScoreboardManager;
import lunaris.rpg.npc.NPC;
import lunaris.rpg.npc.dialogue.DialogueOption;
import lunaris.rpg.npc.dialogue.NPCDialogue;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ModuleInfo(name = "Quests")
public class QuestManager implements IModule {
    private final Lunaris plugin;
    private final QuestRepository questRepository;
    private final Map<UUID, PlayerQuest> activeQuests;
    private final Map<Integer, Quest> questCache;
    private final ScoreboardManager scoreboardManager;
    
    public QuestManager(Lunaris plugin) {
        this.plugin = plugin;
        this.questRepository = new QuestRepository(plugin);
        this.activeQuests = new HashMap<>();
        this.questCache = new HashMap<>();
        this.scoreboardManager = new ScoreboardManager(plugin);
    }
    
    private void loadQuests() {
        try {
            questRepository.getAllQuests().forEach(quest -> 
                questCache.put(quest.getQuestId(), quest));
        } catch (SQLException e) {
            plugin.getLogger().severe("[err->quests]: cant load quests: " + e.getMessage());
        }
    }
    
    public Optional<Quest> getQuest(int questId) {
        return Optional.ofNullable(questCache.get(questId));
    }
    
    public Optional<PlayerQuest> getActiveQuest(Player player) {
        return Optional.ofNullable(activeQuests.get(player.getUniqueId()));
    }
    
    public void startQuest(Player player, int questId) {
        Quest quest = questCache.get(questId);
        if (quest == null) return;
        
        Account account = plugin.getModuleManager()
            .getModule(AccountModule.class)
            .map(module -> module.getAccountService().getOrCreateAccount(player))
            .orElseThrow(() -> new IllegalStateException("acc_not_found"));
            
        if (quest.getRequiredClass() != PlayerClass.NONE && 
            quest.getRequiredClass() != account.getPlayerClass()) {
            player.sendMessage("§cBu görevi almak için doğru sınıfta değilsin!");
            return;
        }
        
        if (account.getLevel() < quest.getRequiredLevel()) {
            player.sendMessage("§cBu görevi almak için yeterli seviyede değilsin!");
            return;
        }
        
        for (int prereqId : quest.getPrerequisiteQuestIds()) {
            try {
                if (!questRepository.isQuestCompleted(player.getUniqueId(), prereqId)) {
                    player.sendMessage("§cBu görevi almak için önceki görevleri tamamlamalısın!");
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        }
        
        PlayerQuest playerQuest = new PlayerQuest(player.getUniqueId(), questId);
        activeQuests.put(player.getUniqueId(), playerQuest);
        
        try {
            questRepository.savePlayerQuest(playerQuest);
            
            player.sendMessage("");
            player.sendMessage("§e§lYeni Görev: §f" + quest.getTitle());
            player.sendMessage("§7" + quest.getDescription());
            if (quest.getHint() != null && !quest.getHint().isEmpty()) {
                player.sendMessage("§7İpucu: §f" + quest.getHint());
            }
            player.sendMessage("");
            
            scoreboardManager.updateScoreboard(player);
            
        } catch (SQLException e) {
            player.sendMessage("§cGörev başlatılırken bir hata oluştu!");
            e.printStackTrace();
        }
    }
    
    public void completeQuest(Player player) {
        PlayerQuest playerQuest = activeQuests.get(player.getUniqueId());
        if (playerQuest == null) return;
        
        Quest quest = questCache.get(playerQuest.getQuestId());
        if (quest == null) return;
        
        playerQuest.setStatus(QuestStatus.COMPLETED);
        
        try {
            questRepository.savePlayerQuest(playerQuest);
            activeQuests.remove(player.getUniqueId());
            
            quest.getRewards().giveRewards(player);
            
            player.sendMessage("");
            player.sendMessage("§6§lGörev Tamamlandı!");
            player.sendMessage("§7" + quest.getTitle());
            player.sendMessage("");
            
            scoreboardManager.updateScoreboard(player);
            
        } catch (SQLException e) {
            player.sendMessage("§cGörev tamamlanırken bir hata oluştu!");
            e.printStackTrace();
        }
    }
    
    @Override
    public void onEnable() {
        loadQuests();
        
        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onProfileCreate(PlayerProfileCreateEvent event) {
                Player player = event.getPlayer();
                Account account = event.getAccount();
                
                Location valorianLoc = new Location(player.getWorld(), -199.312, 73.0, 79.450);
                NPC valorian = plugin.getNPCManager().createNPC("Valorian", valorianLoc);
                
                NPCDialogue dialogue = new NPCDialogue("Valorian")
                    .addLine("Hoş geldin, gezgin! Seni bekliyordum.")
                    .addLine("Görüyorum ki yeni bir maceraya atılmaya hazırsın.",
                        new DialogueOption("accept", "Evet, hazırım!", p -> {
                            getActiveQuest(p).ifPresent(playerQuest -> {
                                getQuest(playerQuest.getQuestId()).ifPresent(quest -> {
                                    quest.getObjectives().stream()
                                        .filter(obj -> obj instanceof TalkToNPCObjective)
                                        .findFirst()
                                        .ifPresent(obj -> obj.onProgress(p, playerQuest));
                                });
                            });
                        }),
                        new DialogueOption("reject", "Henüz değil.", null)
                    );
                
                valorian.setDialogue(dialogue);
                
                Quest firstQuest = new Quest.Builder()
                    .questId(1)
                    .title("Valorian ile konuş")
                    .description("§7Köy yerinde §fValorian'ı §7bul ve konuş!")
                    .hint("§f[-314, 75, -1602]")
                    .objective(new TalkToNPCObjective("Valorian"))
                    .requiredClass(account.getPlayerClass())
                    .requiredLevel(1)
                    .rewards(new QuestRewards.Builder()
                        .experience(100)
                        .money(50)
                        .build())
                    .build();
                
                questCache.put(firstQuest.getQuestId(), firstQuest);
                startQuest(player, firstQuest.getQuestId());
            }
        }, plugin);
    }
    
    @Override
    public void onDisable() {
        activeQuests.clear();
        questCache.clear();
    }
    
    @Override
    public String getName() {
        return "Quests";
    }
    
    @Override
    public ModuleInfo getInfo() {
        return getClass().getAnnotation(ModuleInfo.class);
    }
} 