package lunaris.rpg.quests;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lunaris.core.account.Account;
import lunaris.core.account.AccountService;

import java.util.ArrayList;
import java.util.List;

public class QuestRewards {
    private final int experience;
    private final int money;
    private final List<ItemStack> items;
    
    private QuestRewards(Builder builder) {
        this.experience = builder.experience;
        this.money = builder.money;
        this.items = builder.items;
    }
    
    public void giveRewards(Player player) {
        if (experience > 0) {
        }
        
        if (money > 0) {
        }
        
        if (!items.isEmpty()) {
            items.forEach(item -> player.getInventory().addItem(item));
        }
    }
    
    public static class Builder {
        private int experience = 0;
        private int money = 0;
        private List<ItemStack> items = new ArrayList<>();
        
        public Builder experience(int experience) {
            this.experience = experience;
            return this;
        }
        
        public Builder money(int money) {
            this.money = money;
            return this;
        }
        
        public Builder addItem(ItemStack item) {
            this.items.add(item);
            return this;
        }
        
        public QuestRewards build() {
            return new QuestRewards(this);
        }
    }
} 