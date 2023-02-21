package me.ceridev.duels.manager.kits;

import me.ceridev.duels.instance.KitType;
import me.ceridev.duels.manager.MongoManager;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class KitManager {
    private final MongoManager mongoManager;
    private List<Kit> kits;
    public KitManager(MongoManager mongoManager) {
        this.mongoManager = mongoManager;
        this.kits = new ArrayList<>();
    }
    public void populateKits() {
        for (Document doc : mongoManager.getAllKits()) {
            // items
            List<Material> materials = new ArrayList<>();
            List<Integer> materialAmounts = new ArrayList<>();
            List l = doc.getEmbedded(Collections.singletonList("items"), ArrayList.class);
            String kitTypeAsString = doc.getString("kitType").toUpperCase();
            l.forEach(item -> {
                Document doc1 = (Document) item;
                JSONObject newObj = (JSONObject) JSONValue.parse(doc1.toJson());
                final int[] objectMaterialAmount = {0};
                final Material[] objectMaterial = new Material[1];
                newObj.values().forEach(value -> {
                    objectMaterialAmount[0] = Integer.parseInt(value.toString());
                });
                // gets key from value
                newObj.keySet().forEach(value -> {
                    objectMaterial[0] = Material.valueOf(value.toString());
                });
                materialAmounts.add(objectMaterialAmount[0]);
                materials.add(objectMaterial[0]);

            });
            // armor
            List<ItemStack> armorPieces = new ArrayList<>();
            if (doc.getEmbedded(Collections.singletonList("armor"), ArrayList.class) != null) {
                List al = doc.getEmbedded(Collections.singletonList("armor"), ArrayList.class);
                al.forEach(item -> {
                    Document doc1 = (Document) item;
                    JSONObject newObj = (JSONObject) JSONValue.parse(doc1.toJson());
                    Material armorMaterial = Material.valueOf(newObj.get("item").toString());
                    Map<String, Long> enchants = (Map<String, Long>) newObj.get("enchants");
                    ItemStack armorPiece = new ItemStack(armorMaterial);
                    enchants.forEach((enchant, power) -> {
                        System.out.println(enchant);
                        System.out.println(power);
                        armorPiece.addEnchantment(Enchantment.getByName(enchant), Math.toIntExact(power));
                    });
                    armorPieces.add(armorPiece);
                });
            }
            kits.add(new Kit(KitType.valueOf(kitTypeAsString), materials, materialAmounts, armorPieces));
        }
    }

    public Kit retrieveKit(KitType kitType) {
        for (Kit kit : this.kits) {
            if (kit.getKitType() == kitType) {
                return kit;
            }
        }
        return null;
    }

}
