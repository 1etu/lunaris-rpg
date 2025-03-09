package lunaris.rpg.npc.presets;

import lunaris.core.Lunaris;
import lunaris.rpg.npc.NPC;
import lunaris.rpg.npc.dialogue.DialogueOption;
import lunaris.rpg.npc.dialogue.NPCDialogue;
import lunaris.rpg.npc.NPCManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NPCPresetManager {
    private final Lunaris plugin;
    private final NPCManager npcManager;
    private final Map<String, NPCPreset> presets;
    private final Map<String, NPC> loadedNPCs = new HashMap<>();

    public NPCPresetManager(Lunaris plugin, NPCManager npcManager) {
        this.plugin = plugin;
        this.npcManager = npcManager;
        this.presets = new HashMap<>();
        loadPresets();
    }

    private void loadPresets() {
        File presetsFolder = new File(plugin.getDataFolder(), "npcs");
        if (!presetsFolder.exists()) {
            presetsFolder.mkdirs();
            createDefaultPresets();
        }
        
        for (File file : presetsFolder.listFiles((dir, name) -> name.endsWith(".yml"))) {
            try {
                loadNPCFromFile(file);
            } catch (Exception e) {
                plugin.getLogger().warning("can't load from file, prolly broken structure: " + file.getName());
                e.printStackTrace();
            }
        }
    }

    private void loadNPCFromFile(File file) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        String id = config.getString("id");
        String name = config.getString("name");
        EntityType entityType = EntityType.valueOf(config.getString("entity-type"));
        
        String skinPlayerName = config.getString("skin");
        
        String worldName = config.getString("location.world");
        World world = plugin.getServer().getWorld(worldName);
        if (world == null) {
            throw new IllegalStateException(worldName + " not found for " + id);
        }
        
        Location location = new Location(
            world,
            config.getDouble("location.x"),
            config.getDouble("location.y"),
            config.getDouble("location.z"),
            (float) config.getDouble("location.yaw"),
            (float) config.getDouble("location.pitch")
        );
        
        NPCDialogue dialogue = createDialogueFromConfig(config, name);
        NPCPreset preset = new NPCPreset(id, name, entityType, dialogue, null, skinPlayerName);
        registerPreset(preset);
        
        NPC npc = preset.createNPC(location, -1);  
        loadedNPCs.put(id, npc);
    }

    private NPCDialogue createDialogueFromConfig(FileConfiguration config, String npcName) {
        NPCDialogue dialogue = new NPCDialogue(npcName);
        
        if (config.contains("dialogue.greeting")) {
            String greeting = config.getString("dialogue.greeting");
            List<DialogueOption> options = new ArrayList<>();
            
            for (Map<?, ?> optionMap : config.getMapList("dialogue.options")) {
                String optionId = (String) optionMap.get("id");
                String text = (String) optionMap.get("text");
                String command = (String) optionMap.get("command");
                
                options.add(new DialogueOption(optionId, text, player -> {
                    if (command != null) {
                        plugin.getServer().dispatchCommand(player, command);
                    }
                }));
            }
            
            dialogue.addLine(greeting, options.toArray(new DialogueOption[0]));
        } else if (config.contains("dialogue.lines")) {
            for (Map<?, ?> lineMap : config.getMapList("dialogue.lines")) {
                String text = (String) lineMap.get("text");
                if (lineMap.containsKey("options")) {
                    List<DialogueOption> options = new ArrayList<>();
                    for (Map<?, ?> optionMap : (List<Map<?, ?>>) lineMap.get("options")) {
                        String optionId = (String) optionMap.get("id");
                        String optionText = (String) optionMap.get("text");
                        String command = (String) optionMap.get("command");
                        
                        options.add(new DialogueOption(optionId, optionText, player -> {
                            if (command != null) {
                                plugin.getServer().dispatchCommand(player, command);
                            }
                        }));
                    }
                    dialogue.addLine(text, options.toArray(new DialogueOption[0]));
                } else {
                    dialogue.addLine(text);
                }
            }
        }
        
        return dialogue;
    }

    private void createDefaultPresets() {
        plugin.saveResource("npcs/merchant.yml", false);
        plugin.saveResource("npcs/quest_giver.yml", false);
        plugin.saveResource("npcs/valorian.yml", false);
    }

    private void registerDefaultPresets() {
        NPCDialogue merchantDialogue = new NPCDialogue("Tüccar")
                .addLine("Hoşgeldin! Sana yardımcı olabilir miyim?", 
                        new DialogueOption("shop", "Ürünlerini göster", player -> {
                            // for ex: open shop (we should work on NPC types like merchant etc but idk when, too lazy)
                        }),
                        new DialogueOption("goodbye", "Görüşürüz", null));

        NPCPreset merchantPreset = new NPCPreset(
                "merchant",
                "Tüccar",
                EntityType.VILLAGER,
                merchantDialogue,
                null,
                "Notch"
        );

        NPCDialogue questGiverDialogue = new NPCDialogue("Görev Ustası")
                .addLine("Maceracı! Tam da seni arıyordum.")
                .addLine("Köyümüz tehlikede ve yardımına ihtiyacımız var.", 
                        new DialogueOption("accept_quest", "Görevi kabul et", player -> {
                        }),
                        new DialogueOption("decline", "Şu an müsait değilim", null));

        NPCPreset questGiverPreset = new NPCPreset(
                "quest_giver",
                "Görev Ustası",
                EntityType.PLAYER,
                questGiverDialogue,
                null,
                "liveyourmovie"
        );

        NPCDialogue valorianDialogue = new NPCDialogue("Valorian")
            .addLine("Hoş geldin yeni maceraperest! Ben Valorian, senin rehberinim.");

        NPCPreset valorianPreset = new NPCPreset(
            "Valorian",
            "Valorian",
            EntityType.PLAYER,
            valorianDialogue,
            null,
            "Steve"
        );

        registerPreset(merchantPreset);
        registerPreset(questGiverPreset);
        registerPreset(valorianPreset);
    }

    public void registerPreset(NPCPreset preset) {
        presets.put(preset.getId(), preset);
    }

    public NPCPreset getPreset(String id) {
        return presets.get(id);
    }

    public void spawnAllNPCs() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            spawnNPCsForPlayer(player);
        }
    }

    public void spawnNPCsForPlayer(Player player) {
        for (NPC npc : loadedNPCs.values()) {
            plugin.getNPCManager().spawnNPC(player, npc);
        }
    }

    public Map<String, NPC> getLoadedNPCs() {
        return Collections.unmodifiableMap(loadedNPCs);
    }

    public Map<String, NPCPreset> getPresets() {
        return Collections.unmodifiableMap(presets);
    }
} 