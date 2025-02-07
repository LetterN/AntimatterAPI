package muramasa.antimatter.worldgen.object;

import com.google.gson.JsonObject;
import muramasa.antimatter.Data;
import muramasa.antimatter.material.Material;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class WorldGenOreSmall extends WorldGenBase<WorldGenOreSmall> {

    private final int minY;
    private final int maxY;
    private final int amount;
    private final Material material;

    @SafeVarargs
    public WorldGenOreSmall(String id, int minY, int maxY, int amount, Material material, ResourceKey<Level>... dims) {
        super(id, WorldGenOreSmall.class, dims);
        this.minY = minY;
        this.maxY = maxY;
        this.amount = amount;
        this.material = material;
    }

    @SafeVarargs
    public WorldGenOreSmall(int minY, int maxY, int amount, Material material, ResourceKey<Level>... dims) {
        this(material.getId(), minY, maxY, amount, material, dims);
    }

    @Override
    public WorldGenOreSmall onDataOverride(JsonObject json) {
        super.onDataOverride(json);
        //if (json.has("minY")) minY = Utils.parseInt(dataMap.get("minY"), minY);
        //if (json.has("maxY")) maxY = Utils.parseInt(dataMap.get("maxY"), maxY);
        //if (json.has("amount")) amount = Utils.parseInt(dataMap.get("amount"), amount);
        return this;
    }

    @Override
    public WorldGenOreSmall build() {
        super.build();
        if (material == null) {
            throw new IllegalArgumentException("WorldGenOreVein - " + getId() + ": material does not exist / is null");
        }
        if (!material.has(Data.ORE_SMALL)) {
            throw new IllegalArgumentException("WorldGenOreVein - " + getId() + ": " + material.getId() + " doesn't have the ORE_SMALL tag");
        }
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public Material getMaterial() {
        return material;
    }
}