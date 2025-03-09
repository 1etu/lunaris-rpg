package lunaris.rpg.npc;

import com.comphenix.protocol.wrappers.WrappedGameProfile;

import lunaris.rpg.npc.dialogue.NPCClickHandler;
import lunaris.rpg.npc.dialogue.NPCDialogue;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public class NPC {
    private final int entityId;
    private final UUID uuid;
    private final String name;
    private final Location location;
    private final WrappedGameProfile gameProfile;
    private final EntityType entityType;
    private NPCDialogue dialogue;
    private NPCClickHandler clickHandler;

    private NPC(Builder builder) {
        this.entityId = builder.entityId;
        this.uuid = builder.uuid;
        this.name = builder.name;
        this.location = builder.location;
        this.gameProfile = builder.gameProfile;
        this.entityType = builder.entityType;
        this.dialogue = builder.dialogue;
        this.clickHandler = builder.clickHandler;
    }

    public int getEntityId() {
        return entityId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public WrappedGameProfile getGameProfile() {
        return gameProfile;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public NPCDialogue getDialogue() {
        return dialogue;
    }

    public void setDialogue(NPCDialogue dialogue) {
        this.dialogue = dialogue;
    }

    public NPCClickHandler getClickHandler() {
        return clickHandler;
    }

    public void setClickHandler(NPCClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    public static class Builder {
        private int entityId;
        private UUID uuid;
        private String name;
        private Location location;
        private WrappedGameProfile gameProfile;
        private EntityType entityType;
        private NPCDialogue dialogue;
        private NPCClickHandler clickHandler;

        public Builder entityId(int entityId) {
            this.entityId = entityId;
            return this;
        }

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        public Builder gameProfile(WrappedGameProfile gameProfile) {
            this.gameProfile = gameProfile;
            return this;
        }

        public Builder entityType(EntityType entityType) {
            this.entityType = entityType;
            return this;
        }

        public Builder dialogue(NPCDialogue dialogue) {
            this.dialogue = dialogue;
            return this;
        }

        public Builder clickHandler(NPCClickHandler clickHandler) {
            this.clickHandler = clickHandler;
            return this;
        }

        public NPC build() {
            return new NPC(this);
        }
    }
} 