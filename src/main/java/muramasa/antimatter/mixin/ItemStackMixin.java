package muramasa.antimatter.mixin;

import muramasa.antimatter.AntimatterAPI;
import muramasa.antimatter.Ref;
import muramasa.antimatter.tool.IAntimatterArmor;
import muramasa.antimatter.tool.IAntimatterTool;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin extends net.minecraftforge.common.capabilities.CapabilityProvider<ItemStack> {
    protected ItemStackMixin(Class<ItemStack> baseClass) {
        super(baseClass);
    }

    @Inject(method = "hurtAndBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;", ordinal = 1))
    public <T extends LivingEntity> void inject(int amount, T entity, Consumer<T> consumer, CallbackInfo ci) {
        ItemStack invoker = ((ItemStack) (Object) this);
        if (invoker.getItem() instanceof IAntimatterTool) {
            if (entity instanceof Player) {
                ((IAntimatterTool) invoker.getItem()).onItemBreak(invoker, (Player) entity);
            }
        }
        if (invoker.getItem() instanceof IAntimatterArmor) {
            IAntimatterArmor armor = (IAntimatterArmor) invoker.getItem();
            if (armor.getAntimatterArmorType().getSlot() == EquipmentSlot.HEAD && AntimatterAPI.isModLoaded(Ref.MOD_TOP)) {
                if (invoker.getTag() != null && invoker.getTag().contains("theoneprobe") && invoker.getTag().getBoolean("theoneprobe")) {
                    if (entity instanceof Player) {
                        ItemStack probe = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Ref.MOD_TOP, "probe")));
                        if (!((Player) entity).addItem(probe)) {
                            ((Player) entity).drop(probe, false);
                        }
                    }
                }
            }
        }
    }
}
