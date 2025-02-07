package muramasa.antimatter.integration.kubejs;

import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import muramasa.antimatter.recipe.ingredient.RecipeIngredient;

public class RecipeIngredientJS implements IngredientJS {

    private IngredientJS sourceIngredient;
    private int count;
    private boolean nonConsume;
    private boolean ignoreNbt;

    private RecipeIngredientJS(IngredientJS source) {
        this.sourceIngredient = source;
    }

    public static RecipeIngredientJS of(IngredientJS json) {
        RecipeIngredientJS js = new RecipeIngredientJS(json);
        js.count = json.getCount();
        return js;
    }

    public static RecipeIngredientJS of(JsonElement json) {
        if (!(json instanceof JsonObject)) {
            throw new RuntimeException("invalid input to RecipeIngredientJS");
        }
        JsonObject obj = (JsonObject) json;
        RecipeIngredientJS r;
        if (obj.has("ingredient")) {
            r = new RecipeIngredientJS(IngredientJS.of(obj.get("ingredient")));
        } else {
            r = new RecipeIngredientJS(IngredientJS.of(obj));
        }
        r.count = obj.has("count") ? obj.get("count").getAsInt() : 1;
        r.ignoreNbt = obj.has("nbt") && !obj.get("nbt").getAsBoolean();
        r.nonConsume = obj.has("consume") && !obj.get("consume").getAsBoolean();
        return r;
    }

    public RecipeIngredient into() {
        RecipeIngredient r = new RecipeIngredient(sourceIngredient.createVanillaIngredient(), count);
        if (nonConsume) r.setNoConsume();
        if (ignoreNbt) r.setIgnoreNbt();
        return r;
    }

    @Override
    public JsonElement toJson() {
        JsonElement json = IngredientJS.super.toJson();
        JsonObject obj = new JsonObject();
        obj.add("ingredient", json);
        obj.addProperty("count", count);
        obj.addProperty("nbt", !ignoreNbt);
        obj.addProperty("consume", !nonConsume);
        return obj;
    }

    @Override
    public Set<ItemStackJS> getStacks() {
        return sourceIngredient.getStacks();
    }

    @Override
    public boolean test(ItemStackJS itemStackJS) {
        return sourceIngredient.test(itemStackJS);
    }
}

