package lunaris.rpg.custom_items.armors;

import lunaris.rpg.custom_items.RPGItem;
import lunaris.rpg.custom_items.stats.ItemStat;

public class RPGArmor extends RPGItem {
    private final ArmorType armorType;
    private final int defense;
    private final int magicDefense;

    private RPGArmor(Builder builder) {
        super(builder);
        this.armorType = builder.armorType;
        this.defense = builder.defense;
        this.magicDefense = builder.magicDefense;
    }

    public static class Builder extends RPGItem.Builder<Builder> {
        private ArmorType armorType;
        private int defense;
        private int magicDefense;

        public Builder armorType(ArmorType type) {
            this.armorType = type;
            return this;
        }

        public Builder defense(int defense) {
            this.defense = defense;
            return this;
        }

        public Builder magicDefense(int magicDefense) {
            this.magicDefense = magicDefense;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public RPGArmor build() {
            addStat(ItemStat.DEFENSE, String.valueOf(defense));
            addStat(ItemStat.MAGIC_DEFENSE, String.valueOf(magicDefense));
            return new RPGArmor(this);
        }
    }
} 