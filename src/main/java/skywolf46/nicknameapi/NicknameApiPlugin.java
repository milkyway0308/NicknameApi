package skywolf46.nicknameapi;

import org.bukkit.plugin.java.JavaPlugin;

public class NicknameApiPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        NicknameApiCore.useInMemoryApi(this);
    }
}
