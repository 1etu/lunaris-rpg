package lunaris.rpg.npc.dialogue;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NPCDialogue {
    private final List<DialogueLine> lines;
    private final String npcName;

    public NPCDialogue(String npcName) {
        this.npcName = npcName;
        this.lines = new ArrayList<>();
    }

    public NPCDialogue addLine(String text) {
        lines.add(new DialogueLine(text));
        return this;
    }

    public NPCDialogue addLine(String text, DialogueOption... options) {
        lines.add(new DialogueLine(text, options));
        return this;
    }

    public void start(Player player) {
        showLine(player, 0);
    }

    private void showLine(Player player, int index) {
        if (index >= lines.size()) return;

        DialogueLine line = lines.get(index);
        
        player.sendMessage("");
        player.sendMessage("§e" + npcName + "§7: §f" + line.getText());
        
        if (line.hasOptions()) {
            player.sendMessage("§7Seçenekler:");
            for (DialogueOption option : line.getOptions()) {
                TextComponent message = new TextComponent("§8» §f" + option.getText());
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, 
                    "/npc dialogue " + option.getId()));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
                    new ComponentBuilder("§7Tıkla!").create()));
                player.spigot().sendMessage(message);
            }
        } else if (index < lines.size() - 1) {
            TextComponent next = new TextComponent("§8» §7Devam et...");
            next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, 
                "/npc dialogue next"));
            next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
                new ComponentBuilder("§7Tıkla!").create()));
            player.spigot().sendMessage(next);
        }
    }

    private static class DialogueLine {
        private final String text;
        private final DialogueOption[] options;

        public DialogueLine(String text) {
            this(text, new DialogueOption[0]);
        }

        public DialogueLine(String text, DialogueOption... options) {
            this.text = text;
            this.options = options;
        }

        public String getText() {
            return text;
        }

        public boolean hasOptions() {
            return options.length > 0;
        }

        public DialogueOption[] getOptions() {
            return options;
        }
    }
} 