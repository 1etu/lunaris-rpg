package lunaris.rpg.custom_items.weapons.mythical_spear.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpearAbility implements Listener {
    
    @EventHandler
    public void onSpearUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
    }
} 