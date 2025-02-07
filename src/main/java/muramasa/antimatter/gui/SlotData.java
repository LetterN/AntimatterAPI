package muramasa.antimatter.gui;

import net.minecraft.world.inventory.Slot;

public class SlotData<T extends Slot> {

    private final SlotType<T> type;
    private final int x;
    private final int y;
    private int data = -1;

    public SlotData(SlotType<T> type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public SlotData(SlotType<T> type, int x, int y, int data) {
        this(type, x, y);
        this.data = data;
    }

    public SlotType<T> getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getData() {
        return data;
    }
}
