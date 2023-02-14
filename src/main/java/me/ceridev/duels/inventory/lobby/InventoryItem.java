package me.ceridev.duels.inventory.lobby;

import jdk.nashorn.internal.objects.annotations.Getter;
import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.inventory.menus.PlayerSettings;
import me.ceridev.duels.manager.PlayerManager;
import me.ceridev.duels.manager.queue.QueueManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryItem extends InventoryItemHandler {

    private DuelPlugin plugin;
    private PlayerManager playerManager;
    private QueueManager queueManager;
    private int invSlot;
    private ItemStack item;
    private String itemName;

    public InventoryItem(DuelPlugin plugin, PlayerManager playerManager, int invSlot, ItemStack item, QueueManager queueManager) {
        super(plugin, playerManager);
        this.plugin = plugin;
        this.playerManager = playerManager;
        this.queueManager = queueManager;
        this.invSlot = invSlot;
        this.item = item;
        this.itemName = item.getItemMeta().getDisplayName();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInHand().getType() == Material.ANVIL && player.getItemInHand().getItemMeta().getDisplayName().equals(itemName)) {
            if (playerManager.getDuelPlayer(event.getPlayer()).isInMatch()) return;
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                PlayerSettings ps = new PlayerSettings(plugin, playerManager);
                ps.openPlayerSettingsInv(player);
            }
        } else if (player.getInventory().getItemInHand().getType() == Material.PAPER && player.getItemInHand().getItemMeta().getDisplayName().equals(itemName)) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                queueManager.addPlayerToQueue(event.getPlayer());
            }
            }

    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        if (playerManager.getDuelPlayer((Player) event.getWhoClicked()).isInMatch()) return;
        if (event.getWhoClicked().getItemInHand().getItemMeta().getDisplayName().equals(itemName)) {
                event.setCancelled(true);
            }
    }

    @EventHandler
    public void itemDropEvent(PlayerDropItemEvent event) {
        if (playerManager.getDuelPlayer(event.getPlayer()).isInMatch()) return;
        if (event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(itemName)) {
            event.setCancelled(true);
        }
    }

    @Getter
    public String getItemName() { return this.item.getItemMeta().getDisplayName(); }

    @Getter
    public ItemStack getItem() { return this.item; }

    @Getter
    public int getInvSlot() { return invSlot; }

}
