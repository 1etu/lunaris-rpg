package lunaris.rpg.custom_items.talismans;

import lunaris.rpg.custom_items.RPGItem;
import lunaris.rpg.custom_items.stats.ItemStat;

public class RPGTalisman extends RPGItem {
    private final TalismanEffect effect;
    private final int power;

    private RPGTalisman(Builder builder) {
        super(builder);
        this.effect = builder.effect;
        this.power = builder.power;
    }

    public static class Builder extends RPGItem.Builder<Builder> {
        private TalismanEffect effect;
        private int power;

        public Builder effect(TalismanEffect effect) {
            this.effect = effect;
            return this;
        }

        public Builder power(int power) {
            this.power = power;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public RPGTalisman build() {
            addStat(ItemStat.TALISMAN_EFFECT, effect.getDisplayName());
            addStat(ItemStat.TALISMAN_POWER, String.valueOf(power));
            return new RPGTalisman(this);
        }
    }
} 