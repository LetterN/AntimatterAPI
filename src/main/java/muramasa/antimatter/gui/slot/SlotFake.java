package muramasa.antimatter.gui.slot;

import muramasa.antimatter.capability.IGuiHandler;
import muramasa.antimatter.capability.item.FakeTrackedItemHandler;
import muramasa.antimatter.capability.machine.MachineItemHandler;
import muramasa.antimatter.gui.SlotType;
import muramasa.antimatter.util.Utils;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

//TODO: Should be used on filters
public class SlotFake extends AbstractSlot<SlotFake> implements IClickableSlot {
    final boolean settable;

    public SlotFake(SlotType<SlotFake> type, IGuiHandler tile, IItemHandler stackHandler, int index, int x, int y, boolean settable) {
        super(type, tile, stackHandler, index, x, y);
        this.settable = settable;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return settable;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return settable;
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        if (settable) {
            return 1;
        }
        return super.getMaxStackSize(stack);
    }

    @Override
    @Nonnull
    public ItemStack remove(int amount) {
        if (!settable || !(this.getItemHandler() instanceof FakeTrackedItemHandler)) return super.remove(amount);
        return MachineItemHandler.extractFromInput(this.getItemHandler(), index, amount, false);
    }

    @Override
    public void onQuickCraft(@Nonnull ItemStack p_75220_1_, @Nonnull ItemStack p_75220_2_) {
    }

    @Override
    public void setChanged() {

    }

    @Override
    public void set(@Nonnull ItemStack stack) {
        super.set(stack);
    }

    public boolean isSettable() {
        return settable;
    }

    public ItemStack clickSlot(int clickedButton, ClickType clickType, Player playerEntity, AbstractContainerMenu container) {
        if (!settable) return ItemStack.EMPTY;
        Inventory playerinventory = playerEntity.getInventory();
        ItemStack itemstack = container.getCarried().copy();
        if ((clickType == ClickType.PICKUP || clickType == ClickType.SWAP) && (clickedButton == 0 || clickedButton == 1)) {

            ItemStack slotStack = this.getItem();
            ItemStack heldStack = container.getCarried().copy();
            if (!slotStack.isEmpty()) {
                itemstack = slotStack.copy();
            }
            this.set(heldStack.isEmpty() ? ItemStack.EMPTY : Utils.ca(this.getMaxStackSize(heldStack), heldStack));
            this.setChanged();
        }
        return itemstack;
    }
}
