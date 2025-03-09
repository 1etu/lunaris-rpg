package lunaris.rpg.custom_items.weapons;

import lunaris.rpg.custom_items.RPGItem;
import lunaris.rpg.custom_items.stats.ItemStat;

public class RPGWeapon extends RPGItem {
    private final String damageType;
    private final int minDamage;
    private final int maxDamage;
    private final double attackSpeed;

    private RPGWeapon(Builder builder) {
        super(builder);
        this.damageType = builder.damageType;
        this.minDamage = builder.minDamage;
        this.maxDamage = builder.maxDamage;
        this.attackSpeed = builder.attackSpeed;
    }

    public static class Builder extends RPGItem.Builder<Builder> {
        private String damageType = "Physical";
        private int minDamage;
        private int maxDamage;
        private double attackSpeed = 1.0;

        public Builder damageType(String damageType) {
            this.damageType = damageType;
            return this;
        }

        public Builder damage(int min, int max) {
            this.minDamage = min;
            this.maxDamage = max;
            return this;
        }

        public Builder attackSpeed(double speed) {
            this.attackSpeed = speed;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public RPGWeapon build() {
            addStat(ItemStat.DAMAGE, minDamage + "-" + maxDamage);
            addStat(ItemStat.ATTACK_SPEED, String.format("%.1f", attackSpeed));
            double dps = (minDamage + maxDamage) / 2.0 * attackSpeed;
            addStat(ItemStat.DPS, String.format("%.1f", dps));
            
            return new RPGWeapon(this);
        }
    }
} 