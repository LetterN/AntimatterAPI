package muramasa.antimatter.proxy;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

public class ServerHandler implements IProxyHandler {

    public ServerHandler() {
    }

    @SuppressWarnings("unused")
    public static void setup(FMLDedicatedServerSetupEvent e) {

    }

    @Override
    public Level getClientWorld() {
        throw new IllegalStateException("Cannot call on server!");
    }

    @Override
    public Player getClientPlayer() {
        throw new IllegalStateException("Cannot call on server!");
    }

}
