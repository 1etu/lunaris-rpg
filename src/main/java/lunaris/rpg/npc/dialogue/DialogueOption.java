package lunaris.rpg.npc.dialogue;

import org.bukkit.entity.Player;

public class DialogueOption {
    private final String id;
    private final String text;
    private final DialogueAction action;

    public DialogueOption(String id, String text, DialogueAction action) {
        this.id = id;
        this.text = text;
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void execute(Player player) {
        if (action != null) {
            action.execute(player);
        }
    }

    @FunctionalInterface
    public interface DialogueAction {
        void execute(Player player);
    }
} 