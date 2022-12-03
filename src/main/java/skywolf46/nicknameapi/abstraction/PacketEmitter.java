package skywolf46.nicknameapi.abstraction;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import skywolf46.nicknameapi.annotations.ThreadSafeMethod;
import skywolf46.nicknameapi.data.NicknameData;

public interface PacketEmitter {
    /**
     * 해당 PacketEmitter 인스턴스가 현재 버전의 버킷과 호환되는지 확인합니다.
     *
     * @return 현재 버킷에서 호환 여부
     */
    boolean isCompatible();

    /**
     * 대상 플레이어들에게 닉네임 데이터를 기준으로 data#getPlayer에 존재하는 UUID를 가진 플레이어의 닉네임을 업데이트시킵니다.
     *
     * @param target 업데이트 대상 플레이어
     * @param data   업데이트 내역
     */
    default void emitPacketTo(Iterable<? extends Player> target, NicknameData data) {
        Player changeTarget = Bukkit.getPlayer(data.getPlayer());
        if (changeTarget == null)
            return;
        for (Player p : target)
            emitPacketTo(changeTarget, p, data);
    }

    /**
     * 해당 플러그인 인스턴스를 기준으로 초기화를 진행합니다.
     *
     * @param pl 초기화를 진행할 플러그인 인스턴스
     */
    void initialize(JavaPlugin pl);

    /**
     * 대상 플레이어에게 닉네임 데이터를 기준으로 지정한 플레이어의 닉네임을 업데이트시킵니다.
     *
     * @param target     닉네임을 업데이트할 플레이어
     * @param emitTarget 변경된 닉네임을 보여줄 플레이어
     * @param data       닉네임 데이터
     */
    void emitPacketTo(Player target, Player emitTarget, NicknameData data);

    /**
     * 대상 닉네임 데이터를 지속 업데이트 대상으로 등록합니다.
     * 등록된 지속 업데이트 대상은 강제 업데이트 호출시, 지속적으로 해당 닉네임으로 변경을 시도합니다.
     *
     * @param data 업데이트 대상
     */
    @ThreadSafeMethod
    void reservePacketIntercept(NicknameData data);

    /**
     * 대상 닉네임 데이터를 지속 업데이트 대상에서 제거합니다.
     *
     * @param data 업데이트 제거 대상
     */
    @ThreadSafeMethod
    void removePacketIntercept(NicknameData data);
}
