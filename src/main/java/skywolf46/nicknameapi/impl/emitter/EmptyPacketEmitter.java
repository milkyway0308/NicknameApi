package skywolf46.nicknameapi.impl.emitter;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import skywolf46.nicknameapi.data.NicknameData;

public class EmptyPacketEmitter extends AbstractPacketEmitter {
    @Override
    public boolean isCompatible() {
        return true;
    }

    @Override
    public void initialize(JavaPlugin pl) {
        // Do nothing
    }

    @Override
    public void emitPacketTo(Player target, Player emitTarget, NicknameData data) {
        // Do nothing
    }

    @Override
    public void removePacketIntercept(NicknameData data) {
        // Do nothing
    }
}
