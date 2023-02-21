package me.ceridev.duels.instance.game;

import me.ceridev.duels.instance.BasicArena;
import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.instance.DuelsPlayer;
import me.ceridev.duels.manager.PlayerManager;
import me.ceridev.duels.manager.game.BlockManager;
import me.ceridev.duels.manager.kits.Kit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class BuildUHCGame extends Game {

    private final BasicArena basicArena;
    private DuelPlugin plugin;
    private final Location spawnLocationOne;
    private final Location spawnLocationTwo;
    private final Kit kit;
    private final BlockManager blockManager;

    public BuildUHCGame(DuelPlugin plugin, BasicArena basicArena, Location spawnLocationOne, Location spawnLocationTwo, Kit kit) {
        super(plugin, basicArena);
        this.plugin = plugin;
        this.basicArena = basicArena;
        this.spawnLocationOne = spawnLocationOne;
        this.spawnLocationTwo = spawnLocationTwo;
        this.kit = kit;
        this.blockManager = new BlockManager(basicArena);
    }

    @Override
    public void onStart() {
        DuelsPlayer playerOne = basicArena.getPlayers().get(0);
        DuelsPlayer playerTwo = basicArena.getPlayers().get(1);

        playerOne.getPlayer().teleport(spawnLocationOne);
        playerOne.getPlayer().setGameMode(GameMode.SURVIVAL);

        playerTwo.getPlayer().teleport(spawnLocationTwo);
        playerTwo.getPlayer().setGameMode(GameMode.SURVIVAL);
        kit.addKitToInventory(playerOne);
        kit.addKitToInventory(playerTwo);

    }

    // event handlers;
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Player playerWhoDied = (Player) event.getEntity();
        Player playerWhoDiedKiller = (Player) event.getDamager();
        PlayerManager aPM = basicArena.getArenaPlayerManager();
        // check if both players are in the current running arena
        if (basicArena.getPlayers().contains(plugin.getPlayerManager().getDuelPlayer(playerWhoDied)) && basicArena.getPlayers().contains(plugin.getPlayerManager().getDuelPlayer(playerWhoDiedKiller))) {
            if (playerWhoDied.getHealth() <= event.getFinalDamage()) {
                playerWhoDied.setHealth(20);
                blockManager.removeAllBlocks();
                basicArena.setWinner(aPM.getDuelPlayer(playerWhoDiedKiller), aPM.getDuelPlayer(playerWhoDied));
            }
        }

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (basicArena.getPlayers().contains(plugin.getPlayerManager().getDuelPlayer(event.getPlayer()))) {
            blockManager.addBlock(event.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (basicArena.getPlayers().contains(plugin.getPlayerManager().getDuelPlayer(event.getPlayer()))) {
            blockManager.removeBlock(event.getBlock().getLocation());
        }
    }

    @EventHandler
    public void hungerDepletionEvent(FoodLevelChangeEvent event) {
        Player p = (Player) event.getEntity();
        p.setFoodLevel(20);
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (basicArena.getPlayers().contains(plugin.getPlayerManager().getDuelPlayer(player))) {
            event.setCancelled(true);
        }
    }

}
