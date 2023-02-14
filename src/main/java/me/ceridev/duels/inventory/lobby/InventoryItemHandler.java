package me.ceridev.duels.inventory.lobby;

import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class InventoryItemHandler implements Listener {

    private final DuelPlugin plugin;
    private final PlayerManager playerManager;
    public InventoryItemHandler(DuelPlugin plugin, PlayerManager playerManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
        Bukkit.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
