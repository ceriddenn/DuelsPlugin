package me.ceridev.duels.commands;

import me.ceridev.duels.manager.ArenaManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {

    private final ArenaManager arenaManager;

    public JoinCommand(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You are not a player!");
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("join")) {
            arenaManager.getArena("lotus").addPlayer(player);
        }
        return false;
    }
}
