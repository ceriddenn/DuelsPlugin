package me.ceridev.duels.manager.game;

import jdk.nashorn.internal.objects.annotations.Setter;
import me.ceridev.duels.instance.BasicArena;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class BlockManager {

    private final List<Location> blocks;

    private final BasicArena basicArena;

    public BlockManager(BasicArena basicArena) {
        this.basicArena = basicArena;
        this.blocks = new ArrayList<>();
    }

    @Setter
    public void addBlock(Location location) {
        if (blocks.contains(location)) return;
        blocks.add(location);
    }

    @Setter
    public void removeBlock(Location location) {
        if (!blocks.contains(location)) return;
        blocks.remove(location);
    }

    @Setter
    public void removeAllBlocks() {
        for (Location loc : blocks) {
            loc.getBlock().setType(Material.AIR);
        }
        blocks.clear();
    }

}
