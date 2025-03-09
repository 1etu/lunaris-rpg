package lunaris.rpg.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import lunaris.core.Lunaris;
import lunaris.rpg.npc.presets.NPCPreset;
import lunaris.rpg.npc.presets.NPCPresetManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class NPCManager {
    private final Lunaris plugin;
    private final ProtocolManager protocolManager;
    private final Map<Integer, NPC> npcs;
    private final AtomicInteger entityIdCounter;
    private NPCPresetManager presetManager;

    public NPCManager(Lunaris plugin) {
        this.plugin = plugin;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.npcs = new HashMap<>();
        this.entityIdCounter = new AtomicInteger(1000);
        this.presetManager = new NPCPresetManager(plugin, this);
        registerPacketListeners();
    }

    private void registerPacketListeners() {
        protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                int entityId = packet.getIntegers().read(0);
                EnumWrappers.EntityUseAction action = packet.getEntityUseActions().read(0);

                NPC npc = npcs.get(entityId);
                if (npc != null && action == EnumWrappers.EntityUseAction.INTERACT) {
                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        handleNPCClick(event.getPlayer(), npc);
                    });
                }
            }
        });
    }

    public NPC createNPC(String presetId, Location location) {
        NPCPreset preset = presetManager.getPreset(presetId);
        if (preset == null) {
            plugin.getErrorManager().handleError("NPC-002", null);
            throw new IllegalArgumentException("unknown: " + presetId);
        }

        int entityId = entityIdCounter.getAndIncrement();
        UUID uuid = UUID.randomUUID();
        
        NPC npc = preset.createNPC(location, entityId);
        npcs.put(entityId, npc);
        return npc;
    }

    public void spawnNPC(Player player, NPC npc) {
        try {
            final NPC finalNpc;
            if (npc.getEntityId() == -1) {
                int newEntityId = entityIdCounter.getAndIncrement();
                npcs.put(newEntityId, npc);
                finalNpc = new NPC.Builder()
                    .entityId(newEntityId)
                    .uuid(npc.getUuid())
                    .name(npc.getName())
                    .location(npc.getLocation())
                    .gameProfile(npc.getGameProfile())
                    .entityType(npc.getEntityType())
                    .dialogue(npc.getDialogue())
                    .clickHandler(npc.getClickHandler())
                    .build();
            } else {
                finalNpc = npc;
            }
    
            PacketContainer infoPacket = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
            List<PlayerInfoData> playerInfoDataList = Collections.singletonList(new PlayerInfoData(
                finalNpc.getGameProfile(),
                0,
                EnumWrappers.NativeGameMode.CREATIVE,
                WrappedChatComponent.fromText(finalNpc.getName())
            ));
            infoPacket.getPlayerInfoDataLists().write(0, playerInfoDataList);
            infoPacket.getModifier().write(0, Collections.singletonList(EnumWrappers.PlayerInfoAction.ADD_PLAYER));
    
            PacketContainer spawnPacket = new PacketContainer(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
            spawnPacket.getIntegers()
                .write(0, finalNpc.getEntityId());
            spawnPacket.getUUIDs()
                .write(0, finalNpc.getUuid());
            spawnPacket.getDoubles()
                .write(0, finalNpc.getLocation().getX())
                .write(1, finalNpc.getLocation().getY())
                .write(2, finalNpc.getLocation().getZ());
            spawnPacket.getBytes()
                .write(0, (byte)(finalNpc.getLocation().getYaw() * 256.0F / 360.0F))
                .write(1, (byte)(finalNpc.getLocation().getPitch() * 256.0F / 360.0F));
    
            
            protocolManager.sendServerPacket(player, infoPacket);
            protocolManager.sendServerPacket(player, spawnPacket);
    
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                try {
                    PacketContainer removeInfo = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
                    removeInfo.getPlayerInfoDataLists().write(0, playerInfoDataList);
                    removeInfo.getModifier().write(0, Collections.singletonList(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER));
                    
                    protocolManager.sendServerPacket(player, removeInfo);
                } catch (Exception e) {
                    plugin.getErrorManager().handleError("NPC-001", player, e);
                }
            }, 20L); 
    
        } catch (Exception e) {
            plugin.getErrorManager().handleError("NPC-001", player, e);
            throw new IllegalStateException("can't spawn npc", e);
        }
    }

    public void removeNPC(int entityId) {
        npcs.remove(entityId);
    }

    private void handleNPCClick(Player player, NPC npc) {
        if (npc.getClickHandler() != null) {
            npc.getClickHandler().onClick(player);
        }
        
        if (npc.getDialogue() != null) {
            npc.getDialogue().start(player);
        }
    }

    public void despawnNPC(Player player, NPC npc) {
        PacketContainer despawnPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        despawnPacket.getIntLists().write(0, List.of(npc.getEntityId()));
        
        try {
            protocolManager.sendServerPacket(player, despawnPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NPCPresetManager getPresetManager() {
        return presetManager;
    }
} 