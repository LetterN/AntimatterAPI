package muramasa.antimatter.gui.slot;

import muramasa.antimatter.capability.IGuiHandler;
import muramasa.antimatter.gui.SlotType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandler;

public class SlotInput extends AbstractSlot<SlotInput> {

    public SlotInput(SlotType<SlotInput> type, IGuiHandler tile, IItemHandler stackHandler, int index, int x, int y) {
        super(type, tile, stackHandler, index, x, y);
    }

    @Override
    public boolean mayPickup(Player player) {
        return true;
    }
}
