package me.ceridev.duels.manager.kits;

import jdk.nashorn.internal.objects.annotations.Getter;
import me.ceridev.duels.instance.DuelsPlayer;
import me.ceridev.duels.instance.KitType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Kit {

    private final KitType kitType;
    private List<Material> materials;
    private List<ItemStack> inventoryItems;
    private final List<Integer> materialAmounts;

    public Kit(KitType kitType, List<Material> materials, List<Integer> materialAmounts) {
        this.kitType = kitType;
        this.materialAmounts = materialAmounts;
        this.inventoryItems = new ArrayList<>();
        this.materials = materials;
        for (int i = 0; i<materials.size(); i++) {
            inventoryItems.add(new ItemStack(materials.get(i), materialAmounts.get(i)));
        }
    }

    @Getter
    public KitType getKitType() { return this.kitType; }

    public void addKitToInventory(DuelsPlayer duelsPlayer) {
        for (ItemStack itemStack : inventoryItems) {
            duelsPlayer.getPlayer().getInventory().addItem(itemStack);
        }
    }

}
