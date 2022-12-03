package skywolf46.nicknameapi;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import skywolf46.nicknameapi.abstraction.NicknameApi;
import skywolf46.nicknameapi.abstraction.PacketEmitter;
import skywolf46.nicknameapi.impl.InMemoryNicknameApi;
import skywolf46.nicknameapi.impl.emitter.AbstractPacketEmitter;
import skywolf46.nicknameapi.impl.emitter.EmptyPacketEmitter;
import skywolf46.nicknameapi.impl.emitter.PacketEmitter1192;
import skywolf46.nicknameapi.util.VersionUtil;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class NicknameApiCore {
    private static AtomicBoolean isInitialized = new AtomicBoolean(false);

    private static JavaPlugin initializer;

    @Getter
    private static PacketEmitter emitter;

    @Getter
    private static NicknameApi api;

    private static final ReentrantLock lock = new ReentrantLock();

    /**
     * NicknameApi에 미리 구현된 PacketEmitter을 등록합니다.
     * 해당 작업은 Api 초기화시에만 단 한번 실행됩니다.
     */
    private static void registerCompatibleEmitter() {
        AbstractPacketEmitter.registerEmitter(new PacketEmitter1192());
    }

    /**
     * 등록된 PacketEmitter 인스턴스중 가장 먼저 등록되었으며, 현재 버전에 호환되는 PacketEmitter을 주 패킷 송신 인스턴스로 설정합니다.
     * 해당 작업은 Api 초기화시에만 단 한번 실행됩니다.
     *
     * @param pl 초기화를 진행할 주 플러그인 인스턴스
     */
    private static void initializeEmitter(JavaPlugin pl) {
        emitter = AbstractPacketEmitter.findCompatibleEmitter();
        if (emitter == null) {
            Bukkit.getConsoleSender().sendMessage("§eNicknameApi §f| §cNo compatible packet emitter found for bukkit version " + VersionUtil.extractVersion());
            emitter = new EmptyPacketEmitter();
        } else {
            Bukkit.getConsoleSender().sendMessage("§eNicknameApi §f| §7Using §f" + emitter.getClass().getSimpleName() + " §7as packet emitter for current bukkit version");
            emitter.initialize(pl);
        }
    }

    /**
     * NicknameApi의 초기화를 실행합니다.
     * 해당 메서드는 한번만 호출되어야 하며, 두번째 호출부터는 오류를 발생시킵니다.
     *
     * @param pl 초기화를 진행할 주 플러그인 인스턴스
     * @throws IllegalStateException 초기화가 2번 이상 실행되었을경우, 해당 오류가 발생합니다.
     */
    public static void init(JavaPlugin pl) {
        try {
            lock.lock();
            if (initializer != null) {
                throw new IllegalStateException("NicknameApi already initialized");
            }
            initializer = pl;
            registerCompatibleEmitter();
            initializeEmitter(pl);
        } finally {
            lock.unlock();
        }
    }

    /**
     * NicknameApi에 미리 구현된 온-메모리 닉네임 Api를 사용합니다.
     * 해당 메서드 사용 후, Api의 교체는 서버 재실행 후에만 가능합니다.
     *
     * @param pl 초기화를 진행할 주 플러그인 인스턴스
     */
    public static void useInMemoryApi(JavaPlugin pl) {
        init(pl);
        setApi(() -> new InMemoryNicknameApi(pl));
    }

    /**
     * NicknameApi에 메인 닉네임 Api를 등록합니다.
     * 해당 메서드 사용 후, Api의 교체는 서버 재실행 후에만 가능합니다.
     *
     * @param apiProvider 닉네임 Api 공급자
     * @throws IllegalStateException 초기화가 2번 이상 실행되었을경우, 해당 오류가 발생합니다.
     */
    public static void setApi(Supplier<NicknameApi> apiProvider) {
        try {
            lock.lock();
            if (api != null)
                throw new IllegalStateException("Api already registered as " + api.getClass().getSimpleName());
            api = apiProvider.get();
        } finally {
            lock.unlock();
        }
    }
}
