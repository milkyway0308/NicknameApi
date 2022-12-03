package skywolf46.nicknameapi.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.Nullable;
import skywolf46.nicknameapi.NicknameApiCore;
import skywolf46.nicknameapi.abstraction.NicknameApi;
import skywolf46.nicknameapi.data.NicknameData;
import skywolf46.nicknameapi.enumerations.NicknameType;
import skywolf46.nicknameapi.listeners.EventListener;

import java.util.*;
import java.util.function.BiConsumer;

public class InMemoryNicknameApi implements NicknameApi {
    private final Map<UUID, NicknameData> nameType = new HashMap<>();

    private final List<BiConsumer<NicknameData, NicknameType[]>> updateTrigger = new ArrayList<>();

    private final List<BiConsumer<NicknameData, NicknameType[]>> removeTrigger = new ArrayList<>();

    private final Scoreboard scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();

    public InMemoryNicknameApi(JavaPlugin pl) {
        Bukkit.getConsoleSender().sendMessage("§eNicknameApi §f| §7Registering §f" + getClass().getSimpleName() + "§7 via §f" + pl.getName());
        Bukkit.getPluginManager().registerEvents(new EventListener(this), pl);
        addUpdateTrigger((data, types) -> {
            Player p = Bukkit.getPlayer(data.getPlayer());
            if (p != null)
                updatePlayer(p);
        });
        addRemoveTrigger((data, types) -> {
            Player p = Bukkit.getPlayer(data.getPlayer());
            if (p != null)
                updatePlayer(p);
        });
    }

    @Override
    public boolean hasNickName(Player p) {
        return hasNickname(p.getUniqueId());
    }

    @Override
    public boolean hasNickname(UUID u) {
        return hasNicknameAtAny(u, NicknameType.values());
    }

    @Override
    public boolean hasNicknameAt(Player p, NicknameType type) {
        return hasNicknameAt(p.getUniqueId(), type);
    }

    @Override
    public boolean hasNicknameAt(UUID u, NicknameType type) {
        return hasNicknameAtAny(u, type);
    }

    @Override
    public boolean hasNicknameAtAny(Player p, NicknameType... type) {
        return hasNicknameAtAny(p.getUniqueId(), type);
    }

    @Override
    public boolean hasNicknameAtAny(UUID u, NicknameType... type) {
        if (!nameType.containsKey(u)) return false;
        NicknameData data = nameType.get(u);
        for (NicknameType specifiedType : type) {
            if (data.getNickname(specifiedType) != null) return true;
        }
        return false;
    }

    @Override
    public boolean hasNicknameAtAll(Player p, NicknameType... type) {
        return false;
    }

    @Override
    public boolean hasNicknameAtAll(UUID u, NicknameType... type) {
        if (!nameType.containsKey(u)) return false;
        NicknameData data = nameType.get(u);
        for (NicknameType specifiedType : type) {
            if (data.getNickname(specifiedType) == null) return false;
        }
        return true;
    }

    @Override
    public void setNickname(Player p, String name) {
        setNickname(p.getUniqueId(), name);
    }

    @Override
    public void setNickname(UUID u, String name) {
        setNickname(u, name, NicknameType.values());
    }

    @Override
    public void setNickname(Player p, String name, NicknameType... type) {
        setNickname(p.getUniqueId(), name, type);
    }

    @Override
    public void setNickname(UUID u, String name, NicknameType... type) {
        NicknameData data = getNicknameData(u);
        data.setNickname(name, type);
    }

    @Override
    public @Nullable String getNickname(Player p, NicknameType type) {
        return getNickname(p.getUniqueId(), type);
    }

    @Override
    public @Nullable String getNickname(UUID u, NicknameType type) {
        return getNicknameData(u).getNickname(type);
    }

    public NicknameData getNicknameData(UUID u) {
        return nameType.computeIfAbsent(u, uid -> new NicknameData(u, updateTrigger, removeTrigger));
    }

    @SuppressWarnings("UnusedReturnValue")
    public InMemoryNicknameApi addUpdateTrigger(BiConsumer<NicknameData, NicknameType[]> trigger) {
        updateTrigger.add(trigger);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public InMemoryNicknameApi addRemoveTrigger(BiConsumer<NicknameData, NicknameType[]> trigger) {
        removeTrigger.add(trigger);
        return this;
    }

    @Override
    public Scoreboard getOccupiedScoreboard() {
        return scoreboard;
    }

    @Override
    public void updatePlayer(Player p) {
        NicknameData data = getNicknameData(p.getUniqueId()).clone();
        NicknameApiCore.getEmitter().reservePacketIntercept(data);
        if (NicknameApiCore.getEmitter() != null && hasNicknameAt(p, NicknameType.DISPLAY))
            NicknameApiCore.getEmitter().emitPacketTo(Bukkit.getOnlinePlayers(), data);
    }

    @Override
    public void resetPlayer(Player p) {
        NicknameApiCore.getEmitter().removePacketIntercept(getNicknameData(p.getUniqueId()));
        for (Player others : Bukkit.getOnlinePlayers()) {
            if (others.getWorld().equals(p.getWorld()))
                NicknameApiCore.getEmitter().emitPacketTo(Bukkit.getOnlinePlayers(), new NicknameData(p.getUniqueId()));
        }
    }
}
