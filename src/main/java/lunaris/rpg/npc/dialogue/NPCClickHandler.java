package lunaris.rpg.npc.dialogue;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface NPCClickHandler {
    void onClick(Player player);
} 