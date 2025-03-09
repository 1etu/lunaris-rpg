package lunaris.rpg.npc.presets;

import lunaris.rpg.npc.NPC;
import lunaris.rpg.npc.dialogue.NPCDialogue;
import lunaris.rpg.npc.dialogue.NPCClickHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import java.util.UUID;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

public class NPCPreset {
    private final String id;
    private final String name;
    private final EntityType entityType;
    private final NPCDialogue dialogue;
    private final NPCClickHandler clickHandler;
    private final String skinPlayerName;

    public NPCPreset(String id, String name, EntityType entityType, NPCDialogue dialogue, 
            NPCClickHandler clickHandler, String skinPlayerName) {
        this.id = id;
        this.name = name;
        this.entityType = entityType;
        this.dialogue = dialogue;
        this.clickHandler = clickHandler;
        this.skinPlayerName = skinPlayerName;
    }

    public String getId() {
        return id;
    }

    public NPC createNPC(Location location, int entityId) {
        UUID uuid = UUID.randomUUID();
        WrappedGameProfile profile;
        
        if (skinPlayerName != null) {
            profile = new WrappedGameProfile(uuid, skinPlayerName);
        } else {
            profile = new WrappedGameProfile(uuid, name);
        }

        return new NPC.Builder()
                .entityId(entityId)
                .name(name)
                .location(location)
                .entityType(entityType)
                .dialogue(dialogue)
                .clickHandler(clickHandler)
                .gameProfile(profile)
                .uuid(uuid)
                .build();
    }
} 