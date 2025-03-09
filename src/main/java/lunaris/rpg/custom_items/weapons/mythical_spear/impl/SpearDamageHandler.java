package lunaris.rpg.custom_items.weapons.mythical_spear.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SpearDamageHandler implements Listener {

    @EventHandler
    public void onSpearDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
    }
} 