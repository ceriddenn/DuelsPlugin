package me.ceridev.duels.inventory.menus;

import de.themoep.inventorygui.GuiStateElement;
import de.themoep.inventorygui.InventoryGui;
import me.ceridev.duels.instance.KitType;
import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.instance.DuelsPlayer;
import me.ceridev.duels.manager.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerSettingsMenu {
    private final DuelPlugin plugin;
    private final PlayerManager playerManager;
    public PlayerSettingsMenu(DuelPlugin plugin, PlayerManager playerManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
    }

    String[] guiSetup = {
            "         ",
            "  d   q  ",
            "         "
    };
    String guiTitle = "Settings";

    public void openPlayerSettingsInv(Player player) {
        InventoryGui gui = new InventoryGui(plugin, player, guiTitle, guiSetup);
        DuelsPlayer duelsPlayer = playerManager.getDuelPlayer(player);
        //ItemStack itemStack = new ItemStack(Material.REDSTONE);
        //ItemMeta im = itemStack.getItemMeta();
        //im.setDisplayName("Duel Requests");
        gui.setFiller(new ItemStack(Material.STAINED_GLASS_PANE, 1));
        GuiStateElement duelRequestElement = new GuiStateElement('d',
                new GuiStateElement.State(
                        change -> {
                            duelsPlayer.setDuelRequestsOpen(false);
                        },
                        "requestsEnabled",
                        new ItemStack(Material.REDSTONE, 1),
                        ChatColor.AQUA + "Duel requests",
                        ChatColor.YELLOW + "Status: " + ChatColor.RED + "CLOSED"
                ),
                new GuiStateElement.State(
                        change -> {
                            duelsPlayer.setDuelRequestsOpen(true);
                        },
                        "requestsDisabled",
                        new ItemStack(Material.GLOWSTONE_DUST, 1),
                        ChatColor.AQUA + "Duel requests",
                        ChatColor.YELLOW + "Status: " + ChatColor.GREEN + "OPEN"
                )
        );
        if (duelsPlayer.getDuelRequestsOpen()) {
            duelRequestElement.setState("requestsDisabled");
        } else {
            duelRequestElement.setState("requestsEnabled");
        }
        gui.addElement(duelRequestElement);

        GuiStateElement preferredArenaQueueElement = new GuiStateElement('q',
            new GuiStateElement.State(
                    change -> {
                        duelsPlayer.setPreferredKitQueueType(KitType.POTPVP);
                    },
                    "classicKit",
                    new ItemStack(Material.REDSTONE, 1),
                    ChatColor.AQUA + "Preferred Queue Type",
                    ChatColor.YELLOW + "Queue: " + ChatColor.GREEN + "CLASSIC",
                    ChatColor.BLUE + "Click to change queue to " + "POTPVP!"

            ),
                new GuiStateElement.State(
                        change -> {
                            duelsPlayer.setPreferredKitQueueType(KitType.CLASSIC);
                        },
                        "potpvpKit",
                        new ItemStack(Material.REDSTONE, 1),
                        ChatColor.AQUA + "Preferred Queue Type",
                        ChatColor.YELLOW + "Queue: " + ChatColor.GREEN + "POTPVP",
                        ChatColor.BLUE + "Click to change queue to " + "CLASSIC!"

                )
        );
        if (duelsPlayer.getPreferredKitQueueType() == KitType.CLASSIC) {
            preferredArenaQueueElement.setState("classicKit");
        } else {
            preferredArenaQueueElement.setState("potpvpKit");
        }
        gui.addElement(preferredArenaQueueElement);

        gui.show(player);
    }

}
