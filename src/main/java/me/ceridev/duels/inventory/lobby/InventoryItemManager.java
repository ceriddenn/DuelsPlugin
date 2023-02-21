package me.ceridev.duels.inventory.lobby;

import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.manager.PlayerManager;
import me.ceridev.duels.manager.queue.QueueManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class InventoryItemManager {

    private DuelPlugin plugin;
    private PlayerManager playerManager;
    private QueueManager queueManager;
    protected List<InventoryItem> lobbyItems;
    protected List<InventoryItem> arenaCleanupItems;
    protected List<InventoryItem> queueItems;

    public InventoryItemManager(DuelPlugin plugin, PlayerManager playerManager, QueueManager queueManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
        this.queueManager = queueManager;
        this.lobbyItems = new ArrayList<>();
        this.arenaCleanupItems = new ArrayList<>();
        this.queueItems = new ArrayList<>();
        setupLobbyItems();
        setupArenaCleanupItems();
        setupQueueItems();
    }

    private void setupArenaCleanupItems() {
        ItemStack paperQueue = new ItemStack(Material.PAPER);
        ItemMeta paperQueueMeta = paperQueue.getItemMeta();
        paperQueueMeta.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "Play Again");
        paperQueue.setItemMeta(paperQueueMeta);
        arenaCleanupItems.add(new InventoryItem(plugin, playerManager, 4, paperQueue, queueManager));
    }

    public void setupLobbyItems() {
        ItemStack playerSettings = new ItemStack(Material.ANVIL);
        ItemMeta playerSettingsMeta = playerSettings.getItemMeta();
        playerSettingsMeta.setDisplayName(ChatColor.BLUE.toString() + ChatColor.BOLD + "My Settings");
        playerSettings.setItemMeta(playerSettingsMeta);

        ItemStack queue = new ItemStack(Material.IRON_SWORD);
        ItemMeta queueMeta = queue.getItemMeta();
        queueMeta.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "Queue");
        queue.setItemMeta(queueMeta);

        ItemStack rankedQueue = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta rankedQueueMeta = rankedQueue.getItemMeta();
        rankedQueueMeta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Ranked Queue");
        rankedQueue.setItemMeta(rankedQueueMeta);

        lobbyItems.add(new InventoryItem(plugin, playerManager, 4, playerSettings, queueManager));
        lobbyItems.add(new InventoryItem(plugin, playerManager, 0, queue, queueManager));
        lobbyItems.add(new InventoryItem(plugin, playerManager, 1, rankedQueue, queueManager));


    }

    public void setupQueueItems() {
        ItemStack leaveQueue = new ItemStack(Material.INK_SACK, (byte)1);
        ItemMeta leaveQueueMeta = leaveQueue.getItemMeta();
        leaveQueueMeta.setDisplayName(ChatColor.RED + "Leave Queue");
        leaveQueue.setItemMeta(leaveQueueMeta);

        queueItems.add(new InventoryItem(plugin, playerManager, 8, leaveQueue, queueManager));
    }

    public void addLobbyItemsToPlayer(Player player) {
        player.getInventory().clear();
        for (InventoryItem lobbyItem : this.lobbyItems) {
            player.getInventory().setItem(lobbyItem.getInvSlot(), lobbyItem.getItem());
        }
    }

    public void addArenaCleanupItemsToPlayer(Player player) {
        player.getInventory().clear();
        for (InventoryItem arenaCleanupItem : this.arenaCleanupItems) {
            player.getInventory().setItem(arenaCleanupItem.getInvSlot(), arenaCleanupItem.getItem());
        }
    }

    public void addQueueItemsToPlayer(Player player) {
        player.getInventory().clear();
        for (InventoryItem queueItem : this.queueItems) {
            player.getInventory().setItem(queueItem.getInvSlot(), queueItem.getItem());
        }
    }

    public void unregisterItemEvents() {
        for (InventoryItem lobbyItem : this.lobbyItems) {
            lobbyItem.unregister();
        }
        for (InventoryItem arenaCleanupItem : this.arenaCleanupItems) {
            arenaCleanupItem.unregister();
        }
    }

}
