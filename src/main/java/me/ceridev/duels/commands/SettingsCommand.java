package me.ceridev.duels.commands;

import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.inventory.menus.PlayerSettingsMenu;
import me.ceridev.duels.manager.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsCommand implements CommandExecutor {

    private DuelPlugin plugin;
    private PlayerManager playerManager;
    public SettingsCommand(DuelPlugin plugin, PlayerManager playerManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("You can't run this command.");
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("settings")) {
            PlayerSettingsMenu ps = new PlayerSettingsMenu(plugin, playerManager);
            ps.openPlayerSettingsInv(player);
            return true;
        }
        return false;
    }
}
