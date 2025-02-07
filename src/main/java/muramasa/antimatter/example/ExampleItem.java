package muramasa.antimatter.example;

import muramasa.antimatter.item.ItemBasic;
import muramasa.antimatter.registration.IAntimatterObject;
import muramasa.antimatter.registration.IModelProvider;
import muramasa.antimatter.registration.ITextureProvider;
import muramasa.antimatter.texture.Texture;
import net.minecraft.world.item.Item;

public class ExampleItem extends ItemBasic implements IAntimatterObject, ITextureProvider, IModelProvider {

    public ExampleItem(String domain, String id, Item.Properties properties) {
        super(domain, id, properties);
    }

    @Override
    public Texture[] getTextures() {
        return new Texture[]{new Texture(getRegistryName().getNamespace(), "item/" + getId())};
    }
}
