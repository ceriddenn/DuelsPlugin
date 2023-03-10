package me.ceridev.duels.listener;

import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.inventory.lobby.InventoryItemManager;
import me.ceridev.duels.manager.PlayerManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final PlayerManager playerManager;
    private final DuelPlugin plugin;
    private final InventoryItemManager inventoryItemManager;

    public PlayerJoinListener(DuelPlugin plugin, PlayerManager playerManager, InventoryItemManager inventoryItemManager) {
        this.playerManager = playerManager;
        this.plugin = plugin;
        this.inventoryItemManager = inventoryItemManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(plugin.getDatabaseFile().getGlobalSpawnLocation());
        playerManager.handlePlayerJoin(player);
        inventoryItemManager.addLobbyItemsToPlayer(player);
    }

}
