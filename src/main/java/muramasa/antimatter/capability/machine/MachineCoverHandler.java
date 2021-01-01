package muramasa.antimatter.capability.machine;

import muramasa.antimatter.Data;
import muramasa.antimatter.Ref;
import muramasa.antimatter.capability.CoverHandler;
import muramasa.antimatter.capability.IMachineHandler;
import muramasa.antimatter.cover.Cover;
import muramasa.antimatter.cover.CoverStack;
import muramasa.antimatter.cover.IRefreshableCover;
import muramasa.antimatter.machine.event.IMachineEvent;
import muramasa.antimatter.tile.TileEntityMachine;
import muramasa.antimatter.tool.AntimatterToolType;
import muramasa.antimatter.util.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import tesseract.api.IRefreshable;

import javax.annotation.Nonnull;
import java.util.Arrays;

import static muramasa.antimatter.Data.*;

public class MachineCoverHandler<T extends TileEntityMachine> extends CoverHandler<T> implements IMachineHandler {

    public MachineCoverHandler(T tile) {
        super(tile, tile.getValidCovers());
        Arrays.stream(Ref.DIRS).forEach(d -> {
            Direction facing = getTileFacing();
            Direction newDir = Utils.coverRotateFacing(d, facing);
            //Don't use set(), it calls onPlace which might call into Tesseract.
            CoverStack<T> cover = new CoverStack<>(tile.getMachineType().defaultCover(newDir), tile);
            covers.put(d, cover);
            buildLookup(COVERNONE, cover.getCover(), d);
        });
    }

    public Direction getOutputFacing() {
        return lookupSingle(getTile().getMachineType().getOutputCover().getClass());
    }

    public boolean setOutputFacing(Direction side) {
        Direction dir = getOutputFacing();
        //dir == null means no output cover.
        if (dir == null) return false;
        //get this machines output cover type.
        //We cannot call onPlace etc since it stores tile related data, but still need to refresh network.
        Cover cover = getTile().getMachineType().getOutputCover();
        if (side == dir) return true;
        if (getTileFacing() == side && !getTile().getMachineType().allowsFrontCovers()) return false;
        if (!get(side).isEmpty()) return false;
        CoverStack<T> stack = get(dir);
        covers.put(dir, new CoverStack<T>(COVERNONE, getTile()));
        covers.put(side, stack);
        if (cover instanceof IRefreshableCover) {
            ((IRefreshableCover)cover).refresh(stack);
        }
        buildLookup(COVERNONE,cover, side);
        buildLookup(cover,COVERNONE, dir);
        if (this.getTile().getWorld() != null) {
            sync();
            getTile().sidedSync(true);
        }
        return true;
    }

    @Override
    public boolean set(Direction side, @Nonnull Cover newCover) {
        if (getTileFacing() == side && !getTile().getMachineType().allowsFrontCovers()) return false;

        boolean ok = super.set(side, newCover);
        if (ok) {
            getTile().sidedSync(true);
        }
        return ok;
    }

    @Override
    public boolean onInteract(@Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull Direction side, @Nonnull AntimatterToolType type) {
        return super.onInteract(player, hand, side, type);
    }

    @Override
    public void onMachineEvent(IMachineEvent event, Object... data) {
        covers.forEach((s, c) -> c.onMachineEvent(getTile(), event));
    }

    @Override
    public boolean isValid(@Nonnull Direction side, @Nonnull Cover replacement) {
        if (!validCovers.contains(replacement.getId())) return false;
        if (side == getOutputFacing()) return false;
        return (get(side).isEmpty() && !replacement.isEmpty()) || super.isValid(side, replacement);
    }

    @Override
    public Direction getTileFacing() {
        return getTile().getFacing();
    }

}
