package me.ceridev.duels.commands;

import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.instance.KitType;
import me.ceridev.duels.inventory.menus.QueueMenu;
import me.ceridev.duels.manager.queue.QueueManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QueueCommand implements CommandExecutor {
    private QueueManager queueManager;
    private DuelPlugin plugin;
    public QueueCommand(DuelPlugin plugin, QueueManager queueManager) {

        this.queueManager = queueManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You are not a player!");
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("jq")) {
           QueueMenu qm = new QueueMenu(plugin, plugin.getPlayerManager());
           qm.openQueueMenu(player);
        }

        return false;
    }
}
