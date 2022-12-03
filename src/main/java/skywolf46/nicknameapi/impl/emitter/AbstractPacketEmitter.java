package skywolf46.nicknameapi.impl.emitter;

import skywolf46.nicknameapi.abstraction.PacketEmitter;
import skywolf46.nicknameapi.data.NicknameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractPacketEmitter implements PacketEmitter {
    private static final List<AbstractPacketEmitter> registeredEmitter = new ArrayList<>();

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    private final HashMap<UUID, NicknameData> data = new HashMap<>();

    public static AbstractPacketEmitter findCompatibleEmitter() {
        for (AbstractPacketEmitter emitter : registeredEmitter) {
            if (emitter.isCompatible())
                return emitter;
        }
        return null;
    }

    public static void registerEmitter(AbstractPacketEmitter emitter) {
        registeredEmitter.add(emitter);
    }

    @Override
    public void reservePacketIntercept(NicknameData data) {
        setNicknameData(data.getPlayer(), data);
    }

    @Override
    public void removePacketIntercept(NicknameData data) {
        removeNicknameData(data.getPlayer());
    }

    public void setNicknameData(UUID uid, NicknameData nickname) {
        ReentrantReadWriteLock.WriteLock lock = rwLock.writeLock();
        lock.lock();
        data.put(uid, nickname);
        lock.unlock();
    }


    public void removeNicknameData(UUID uid) {
        ReentrantReadWriteLock.WriteLock lock = rwLock.writeLock();
        lock.lock();
        data.remove(uid);
        lock.unlock();
    }

    public NicknameData getNicknameData(UUID uid) {
        ReentrantReadWriteLock.ReadLock lock = rwLock.readLock();
        lock.lock();
        NicknameData nickname = data.get(uid);
        lock.unlock();
        return nickname;
    }


}
