package me.ceridev.duels.inventory.menus;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.instance.KitType;
import me.ceridev.duels.manager.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class QueueMenu {

    private final DuelPlugin plugin;
    private final PlayerManager playerManager;

    public QueueMenu(DuelPlugin plugin, PlayerManager playerManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
    }

    public void openQueueMenu(Player player) {
        List<String> itemMaterials = new ArrayList<>();
        // populate itemMaterials add one for each queue item.
        itemMaterials.add("FISHING_ROD");
        itemMaterials.add("POTION");
        itemMaterials.add("DIAMOND_PICKAXE");
        itemMaterials.add("GOLDEN_APPLE");
        itemMaterials.add("WOOL");
        List<ItemStack> items = new ArrayList<>();
        List<Character> chars = new ArrayList<>();
        chars.add('a');
        chars.add('b');
        chars.add('c');
        chars.add('d');
        chars.add('e');
        int index = 0;
        for (KitType kitType : KitType.values()) {
            ItemStack is = new ItemStack(Material.valueOf(itemMaterials.get(index)));
            ItemMeta isMeta = is.getItemMeta();
            isMeta.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + kitType.toString().substring(0, 1).toUpperCase() + kitType.toString().substring(1));
            is.setItemMeta(isMeta);
            items.add(is);
            index++;
        }
        // setup gui base
        String[] guiSetup = {
                "    b    ",
                "   aec   ",
                "    d    "
        };

        InventoryGui inventoryGui = new InventoryGui(plugin, player, "Queue Menu", guiSetup);
        inventoryGui.setFiller(new ItemStack(Material.STAINED_GLASS_PANE));
        int loopI = 0;
        for (ItemStack itemStack : items) {
            Material m = itemStack.getData().getItemType();
            int finalLoopI = loopI;
            inventoryGui.addElement(new StaticGuiElement(chars.get(loopI),
                    itemStack,
                    1,
                    click -> {
                        plugin.getQueueManager().addPlayerToQueue(player, KitType.valueOf(ChatColor.stripColor(items.get(finalLoopI).getItemMeta().getDisplayName().toUpperCase())));
                        inventoryGui.close();
                        return true;
                    }
            ));
            loopI++;
        }
        inventoryGui.show(player);
    }

}
