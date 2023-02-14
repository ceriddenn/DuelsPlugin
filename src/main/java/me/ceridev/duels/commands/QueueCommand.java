package me.ceridev.duels.commands;

import me.ceridev.duels.manager.queue.QueueManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QueueCommand implements CommandExecutor {
    private QueueManager queueManager;
    public QueueCommand(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You are not a player!");
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("jq")) {
            queueManager.addPlayerToQueue(player);
        }

        return false;
    }
}
