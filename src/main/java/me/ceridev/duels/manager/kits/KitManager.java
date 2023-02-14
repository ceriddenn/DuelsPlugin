package me.ceridev.duels.manager.kits;

import com.mongodb.util.JSON;
import me.ceridev.duels.adapter.MongoAdapter;
import me.ceridev.duels.instance.KitType;
import me.ceridev.duels.manager.MongoManager;
import org.bson.Document;
import org.bukkit.Material;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.*;

public class KitManager {
    private final MongoManager mongoManager;
    private List<Kit> kits;
    public KitManager(MongoManager mongoManager) {
        this.mongoManager = mongoManager;
        this.kits = new ArrayList<>();
    }
    public void populateKits() {
        for (Document doc : mongoManager.getAllKits()) {
            List<Material> materials = new ArrayList<>();
            List<Integer> materialAmounts = new ArrayList<>();
            ArrayList l = doc.getEmbedded(Collections.singletonList("items"), ArrayList.class);
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
            kits.add(new Kit(KitType.valueOf(kitTypeAsString), materials, materialAmounts));
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
