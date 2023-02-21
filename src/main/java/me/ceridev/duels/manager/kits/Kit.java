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
    private List<ItemStack> armorPieces;
    private final List<Integer> materialAmounts;

    public Kit(KitType kitType, List<Material> materials, List<Integer> materialAmounts, List<ItemStack> armorPieces) {
        this.kitType = kitType;
        this.materialAmounts = materialAmounts;
        this.inventoryItems = new ArrayList<>();
        this.armorPieces = armorPieces;
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
        for (ItemStack itemStack : armorPieces) {
            String name = itemStack.getType().name().toLowerCase();
            if (name.contains("helmet")) {
                duelsPlayer.getPlayer().getInventory().setHelmet(itemStack);
            } else if (name.contains("chestplate")) {
                duelsPlayer.getPlayer().getInventory().setChestplate(itemStack);
            } else if (name.contains("leggings")) {
                duelsPlayer.getPlayer().getInventory().setLeggings(itemStack);
            } else if (name.contains("boots")) {
                duelsPlayer.getPlayer().getInventory().setBoots(itemStack);
            } else {
                return;
            }
        }
    }

}
