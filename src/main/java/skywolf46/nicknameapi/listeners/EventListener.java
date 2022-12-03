package skywolf46.nicknameapi.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import skywolf46.nicknameapi.abstraction.NicknameApi;
import skywolf46.nicknameapi.enumerations.NicknameType;

@RequiredArgsConstructor
public class EventListener implements Listener {
    private final NicknameApi api;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        api.updatePlayer(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(PlayerChatEvent e) {
        String name = api.getNickname(e.getPlayer().getUniqueId(), NicknameType.CHAT);
        if (name != null)
            e.setFormat(e.getFormat().replace("%1$s", name));
    }
}
