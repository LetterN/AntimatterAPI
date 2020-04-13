package muramasa.antimatter.capability.impl;

import muramasa.antimatter.tile.TileEntityMachine;
import muramasa.antimatter.util.Utils;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import tesseract.graph.ITickingController;
import tesseract.util.Dir;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FluidTankWrapper implements IFluidHandler {

    protected FluidTankHandler[] tanks;
    public boolean dirty = false;

    public FluidTankWrapper(TileEntityMachine machine, ITickingController controller, int count, int capacity, boolean input) {
        tanks = new FluidTankHandler[count];
        for (int i = 0; i < count; i++) {
            tanks[i] = new FluidTankHandler(capacity, 1000, input ? 0 : 1000) {
                @Override
                public void reset(@Nullable ITickingController oldController, @Nullable ITickingController newController) {

                }

                @Override
                public boolean connects(@Nonnull Dir direction) {
                    return true;
                }

                @Override
                public boolean canOutput(@Nonnull Dir direction) {
                    return !input;
                }

                @Override
                protected void onContentsChanged() {
                    dirty = true;
                    //machine.onContentsChanged(input ? ContentEvent.FLUID_INPUT : ContentEvent.FLUID_OUTPUT, 0);
                }
            };
        }
    }

//    @Override
//    public IFluidTankProperties[] getTankProperties() {
//        IFluidTankProperties[] properties = new IFluidTankProperties[tanks.length];
//        for (int i = 0; i < tanks.length; i++) {
//            properties[i] = new FluidTankProperties(tanks[i].getFluid(), tanks[i].getCapacity(), tanks[i].canFill(), tanks[i].canDrain());
//        }
//        return properties;
//    }

//    @Override
//    public int fill(FluidStack resource, boolean doFill) {
//        FluidTank tank = findFluidInTanks(resource);
//        if (tank != null) {
//            return tank.fill(resource, doFill);
//        } else {
//            tank = getFirstEmptyTank();
//            if (tank != null) return tank.fill(resource, doFill);
//        }
//        return 0;
//    }
//
//    @Nullable
//    @Override
//    public FluidStack drain(FluidStack resource, boolean doDrain) {
//        FluidTank tank = findFluidInTanks(resource);
//        if (tank != null) return tank.drain(resource, doDrain);
//        return null;
//    }
//
//    @Nullable
//    @Override
//    public FluidStack drain(int maxDrain, boolean doDrain) {
//        FluidTank tank = getFirstValidTank();
//        if (tank != null) return tank.drain(maxDrain, doDrain);
//        return null;
//    }

    public void setFirstValidOrEmptyTank(FluidStack fluid) {
        FluidTankHandler tank = getFirstValidTank();
        if (tank == null) tank = getFirstEmptyTank();
        if (tank != null) {
            tank.setFluid(fluid);
        }
    }

    public FluidTankHandler getFirstEmptyTank() {
        for (int i = 0; i < tanks.length; i++) {
            if (tanks[i].getFluid() == null) return tanks[i];
        }
        return null;
    }

    @Nullable
    public FluidTankHandler getFirstValidTank() {
        for (int i = 0; i < tanks.length; i++) {
            if (tanks[i].getFluid() != null) return tanks[i];
        }
        return null;
    }

    @Nullable
    public FluidTankHandler findFluidInTanks(FluidStack fluid) {
        for (int i = 0; i < tanks.length; i++) {
            if (tanks[i].getFluid() != null && Utils.equals(tanks[i].getFluid(), fluid)) return tanks[i];
        }
        return null;
    }

    @Override
    public int getTanks() {
        return 0;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return null;
    }

    @Override
    public int getTankCapacity(int tank) {
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return null;
    }
}
