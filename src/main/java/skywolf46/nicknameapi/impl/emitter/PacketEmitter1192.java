package skywolf46.nicknameapi.impl.emitter;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import skywolf46.nicknameapi.data.NicknameData;
import skywolf46.nicknameapi.enumerations.NicknameType;
import skywolf46.nicknameapi.util.VersionUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("CommentedOutCode")
public class PacketEmitter1192 extends AbstractPacketEmitter {
    private Class<?> CLASS_CRAFT_PLAYER;

    private Class<?> CLASS_ENTITY_PLAYER;

    private Class<?> CLASS_MULTI_MAP;

    private Class<?> CLASS_ENTITY_HUMAN;

    private Class<?> CLASS_PLAYER_CONNECTION;

    private Class<?> CLASS_PACKET;

    private Class<?> CLASS_PROFILE_PUBLIC_KEY;

    private Class<?> CLASS_PROFILE_PUBLIC_KEY_DATA;

    private Class<?> CLASS_GAME_PROFILE;

    private Class<?> CLASS_ENUM_GAME_MODE;

    private Class<?> CLASS_INTERACT_MANAGER;

    private Class<?> CLASS_ICHATBASE;

    private Class<?> CLASS_PACKET_UPDATE_PLAYER_INFO;

    private Class<Enum<?>> CLASS_ENUM_PLAYER_INFO_ACTION;

    private Method MULTI_MAP_PUT_ALL;

    private Method CRAFT_PLAYER_GET_HANDLE;

    private Method ENTITY_PLAYER_GET_PROFILE_PUBLIC_KEY;

    private Field ENTITY_PLAYER_LATENCY;

    private Field ENTITY_PLAYER_PLAYER_CONNECTION;

    private Field ENTITY_PLAYER_INTERACT_MANAGER;

    private Method ENTITY_PLAYER_GET_GAME_PROFILE;

    private Method PROFILE_PUBLIC_KEY_GET_DATA;

    private Method PLAYER_CONNECTION_SEND_PACKET;

    private Method GAME_PROFILE_GET_PROPERTY_MAP;

    private Method PACKET_OUT_PLAYER_INFO_GET_ENTRY;

    private Method INTERACT_MANAGER_GET_ENUM_GAME_MODE;

    private Method ICHATBASE_NEW_LITERAL;

    private Constructor<?> CONSTRUCTOR_GAME_PROFILE;

    private Constructor<?> CONSTRUCTOR_PACKET_UPDATE_PLAYER_INFO;

    private Constructor<?> CONSTRUCTOR_PLAYER_INFO;

    private Constructor<?> CONSTRUCTOR_PACKET_ENTITY_DESTROY;

    private Constructor<?> CONSTRUCTOR_PACKET_NAMED_ENTITY_SPAWN;


    @Override
    public boolean isCompatible() {
        boolean isVersionMatched = VersionUtil.extractVersion().equals("1.19.2");
        try {
            Class.forName("org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return isVersionMatched;
    }

    @Override
    public void initialize(JavaPlugin pl) {
        initReflection();
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onJoin(PlayerJoinEvent e) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () -> {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        NicknameData data = getNicknameData(p.getUniqueId());
                        if (data != null)
                            emitPacketTo(p, e.getPlayer(), data);
                    }
                }, 1L);
            }
        }, pl);
    }

    @SneakyThrows
    private void initReflection() {
        CLASS_CRAFT_PLAYER = Class.forName("org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer");
        CLASS_PLAYER_CONNECTION = Class.forName("net.minecraft.server.network.PlayerConnection");
        CLASS_ENTITY_PLAYER = Class.forName("net.minecraft.server.level.EntityPlayer");
        CLASS_ENTITY_HUMAN = Class.forName("net.minecraft.world.entity.player.EntityHuman");
        CLASS_PACKET_UPDATE_PLAYER_INFO = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo");
        CLASS_PACKET = Class.forName("net.minecraft.network.protocol.Packet");
        CLASS_MULTI_MAP = Class.forName("com.google.common.collect.Multimap");
        CLASS_PROFILE_PUBLIC_KEY = Class.forName("net.minecraft.world.entity.player.ProfilePublicKey");
        CLASS_PROFILE_PUBLIC_KEY_DATA = Class.forName("net.minecraft.world.entity.player.ProfilePublicKey$a");
        CLASS_ENUM_GAME_MODE = Class.forName("net.minecraft.world.level.EnumGamemode");
        CLASS_GAME_PROFILE = Class.forName("com.mojang.authlib.GameProfile");
        CLASS_ICHATBASE = Class.forName("net.minecraft.network.chat.IChatBaseComponent");
        CLASS_INTERACT_MANAGER = Class.forName("net.minecraft.server.level.PlayerInteractManager");
        CLASS_ENUM_PLAYER_INFO_ACTION = (Class<Enum<?>>) Class.forName("net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
        MULTI_MAP_PUT_ALL = CLASS_MULTI_MAP.getMethod("putAll", CLASS_MULTI_MAP);
        CRAFT_PLAYER_GET_HANDLE = CLASS_CRAFT_PLAYER.getMethod("getHandle");
        ENTITY_PLAYER_GET_PROFILE_PUBLIC_KEY = CLASS_ENTITY_PLAYER.getMethod("fz");
        ENTITY_PLAYER_GET_GAME_PROFILE = CLASS_ENTITY_PLAYER.getMethod("fy");
        ENTITY_PLAYER_PLAYER_CONNECTION = CLASS_ENTITY_PLAYER.getField("b");
        ENTITY_PLAYER_LATENCY = CLASS_ENTITY_PLAYER.getField("e");
        ENTITY_PLAYER_INTERACT_MANAGER = CLASS_ENTITY_PLAYER.getField("d");
        PLAYER_CONNECTION_SEND_PACKET = CLASS_PLAYER_CONNECTION.getMethod("a", CLASS_PACKET);
        PROFILE_PUBLIC_KEY_GET_DATA = CLASS_PROFILE_PUBLIC_KEY.getMethod("b");
        GAME_PROFILE_GET_PROPERTY_MAP = CLASS_GAME_PROFILE.getMethod("getProperties");
        ICHATBASE_NEW_LITERAL = CLASS_ICHATBASE.getMethod("b", String.class);
        PACKET_OUT_PLAYER_INFO_GET_ENTRY = CLASS_PACKET_UPDATE_PLAYER_INFO.getMethod("b");
        INTERACT_MANAGER_GET_ENUM_GAME_MODE = CLASS_INTERACT_MANAGER.getMethod("b");
        CONSTRUCTOR_GAME_PROFILE = CLASS_GAME_PROFILE.getConstructor(UUID.class, String.class);
        CONSTRUCTOR_PACKET_UPDATE_PLAYER_INFO = CLASS_PACKET_UPDATE_PLAYER_INFO.getConstructor(
                CLASS_ENUM_PLAYER_INFO_ACTION,
                Array.newInstance(CLASS_ENTITY_PLAYER, 0).getClass()
        );
        CONSTRUCTOR_PLAYER_INFO = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo$PlayerInfoData")
                .getConstructor(CLASS_GAME_PROFILE, int.class, CLASS_ENUM_GAME_MODE, CLASS_ICHATBASE, CLASS_PROFILE_PUBLIC_KEY_DATA);
        CONSTRUCTOR_PACKET_ENTITY_DESTROY = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy")
                .getConstructor(int[].class);
        CONSTRUCTOR_PACKET_NAMED_ENTITY_SPAWN = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn")
                .getConstructor(CLASS_ENTITY_HUMAN);

    }

//    리플렉션 없이 NMS 의존할 시, 원본 코드
//    @Override
//    public void emitPacketTo(Player target, Player emitTarget, NicknameData data) {
//        EntityPlayer emitTo = ((CraftPlayer) emitTarget).getHandle();
//        EntityPlayer sp = ((CraftPlayer) target).getHandle();
//        ProfilePublicKey pubKey = sp.fz();
//        ProfilePublicKey.a keyData = pubKey != null ? pubKey.b() : null;
//        emitTo.b.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, sp));
//        PacketPlayOutPlayerInfo updatePacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, sp);
//        GameProfile origin = sp.fy();
//        GameProfile modified = new GameProfile(origin.getId(), data.getNickname(NicknameType.DISPLAY));
//        modified.getProperties().putAll(origin.getProperties());
//        updatePacket.b().set(0, new PacketPlayOutPlayerInfo.PlayerInfoData(modified, sp.e, sp.d.b(), IChatBaseComponent.b(data.getNickname(NicknameType.DISPLAY)), keyData));
//        emitTo.b.a(updatePacket);
//        if (emitTarget.getUniqueId() != target.getUniqueId()) {
//            emitTo.b.a(new PacketPlayOutEntityDestroy(sp.ae()));
//            emitTo.b.a(new PacketPlayOutNamedEntitySpawn(sp));
//        }
//    }

    @Override
    @SneakyThrows
    public void emitPacketTo(Player target, Player emitTarget, NicknameData data) {
        String targetName = data.getNickname(NicknameType.DISPLAY) != null ? data.getNickname(NicknameType.DISPLAY) : target.getName();
        Object emitTo = asEntityPlayer(emitTarget);
        Object sp = asEntityPlayer(target);
        Object pubKey = ENTITY_PLAYER_GET_PROFILE_PUBLIC_KEY.invoke(sp);
        Object keyData = pubKey != null ? PROFILE_PUBLIC_KEY_GET_DATA.invoke(pubKey) : null;
        sendPacketTo(emitTo, CONSTRUCTOR_PACKET_UPDATE_PLAYER_INFO.newInstance(getPlayerInfoAction(4), createPlayerArray(sp)));
        Object updatePacket = CONSTRUCTOR_PACKET_UPDATE_PLAYER_INFO.newInstance(getPlayerInfoAction(0), createPlayerArray(sp));
        Object originGameProfile = ENTITY_PLAYER_GET_GAME_PROFILE.invoke(sp);
        Object modifiedGameProfile = CONSTRUCTOR_GAME_PROFILE.newInstance(target.getUniqueId(), targetName);
        MULTI_MAP_PUT_ALL.invoke(extractProperty(modifiedGameProfile), extractProperty(originGameProfile));
        extractPlayerInfoEntry(updatePacket)
                .set(0, CONSTRUCTOR_PLAYER_INFO.newInstance(modifiedGameProfile,
                        ENTITY_PLAYER_LATENCY.get(sp),
                        INTERACT_MANAGER_GET_ENUM_GAME_MODE.invoke(ENTITY_PLAYER_INTERACT_MANAGER.get(sp)),
                        ICHATBASE_NEW_LITERAL.invoke(null, targetName),
                        keyData
                ));
        sendPacketTo(emitTo, updatePacket);
        if (emitTarget.getUniqueId() != target.getUniqueId() && emitTarget.getWorld().equals(target.getWorld())) {
            //noinspection RedundantCast
            sendPacketTo(emitTo, CONSTRUCTOR_PACKET_ENTITY_DESTROY.newInstance((Object) new int[]{target.getEntityId()}));
            sendPacketTo(emitTo, CONSTRUCTOR_PACKET_NAMED_ENTITY_SPAWN.newInstance(sp));
        }
    }

    @SneakyThrows
    private Object createPlayerArray(Object entityPlayer) {
        Object arr = Array.newInstance(CLASS_ENTITY_PLAYER, 1);
        Array.set(arr, 0, entityPlayer);
        return arr;
    }

    @SneakyThrows
    private List<Object> extractPlayerInfoEntry(Object packetPlayerInfo) {
        return (List<Object>) PACKET_OUT_PLAYER_INFO_GET_ENTRY.invoke(packetPlayerInfo);
    }

    @SneakyThrows
    private Object extractProperty(Object gameProfile) {
        return GAME_PROFILE_GET_PROPERTY_MAP.invoke(gameProfile);
    }

    private Enum<?> getPlayerInfoAction(int index) {
        return CLASS_ENUM_PLAYER_INFO_ACTION.getEnumConstants()[index];
    }

    @SneakyThrows
    private Object asEntityPlayer(Player p) {
        return CRAFT_PLAYER_GET_HANDLE.invoke(p);
    }

    @SneakyThrows
    private void sendPacketTo(Object who, Object packet) {
        Object connection = ENTITY_PLAYER_PLAYER_CONNECTION.get(who);
        PLAYER_CONNECTION_SEND_PACKET.invoke(connection, packet);
    }
}
