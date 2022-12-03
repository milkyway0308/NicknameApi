package skywolf46.nicknameapi.abstraction;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.Nullable;
import skywolf46.nicknameapi.enumerations.NicknameType;

import java.util.UUID;

public interface NicknameApi {

    /**
     * 대상 플레이어에게 닉네임이 존재하는지 조회합니다.
     * 이는 {@link NicknameApi#hasNicknameAtAll(Player, NicknameType...)}에서 모든 파라미터를 가지고 호출한 결과와 같은 결과를 반환합니다.
     *
     * @param p 대상 플레이어
     * @return 대상 플레이어의 닉네임의 존재 여부
     */
    boolean hasNickName(Player p);

    /**
     * 대상 플레이어에게 닉네임이 존재하는지 조회합니다.
     * 이는 {@link NicknameApi#hasNicknameAtAll(UUID, NicknameType...)}에서 모든 파라미터를 가지고 호출한 결과와 같은 결과를 반환합니다.
     *
     * @param u 대상 플레이어 UUID
     * @return 대상 플레이어의 닉네임의 존재 여부
     */
    boolean hasNickname(UUID u);

    /**
     * 대상 플레이어에게 해당 타입의 이름이 존재하는지 조회합니다.
     *
     * @param p    대상 플레이어
     * @param type 대상 타입
     * @return 대상 플레이어의 지정한 타입 닉네임의 존재 여부
     */
    boolean hasNicknameAt(Player p, NicknameType type);

    /**
     * 대상 플레이어에게 해당 타입의 이름이 존재하는지 조회합니다.
     *
     * @param u    대상 플레이어 UUID
     * @param type 대상 타입
     * @return 대상 플레이어의 지정한 타입 닉네임의 존재 여부
     */
    boolean hasNicknameAt(UUID u, NicknameType type);

    /**
     * 대상 플레이어에게 해당 타입들의 이름이 하나 이상 존재하는지 조회합니다.
     *
     * @param p    대상 플레이어
     * @param type 대상 타입
     * @return 대상 플레이어의 지정한 타입의 닉네임이 하나 이상 존재하는지의 여부
     */
    boolean hasNicknameAtAny(Player p, NicknameType... type);

    /**
     * 대상 플레이어에게 해당 타입들의 이름이 하나 이상 존재하는지 조회합니다.
     *
     * @param u    대상 플레이어
     * @param type 대상 타입
     * @return 대상 플레이어의 지정한 타입의 닉네임이 하나 이상 존재하는지의 여부
     */
    boolean hasNicknameAtAny(UUID u, NicknameType... type);

    /**
     * 대상 플레이어에게 해당 타입들의 이름이 모두 존재하는지 조회합니다.
     *
     * @param p    대상 플레이어
     * @param type 대상 타입
     * @return 대상 플레이어의 지정한 타입의 닉네임이 모두 존재하는지의 여부
     */
    boolean hasNicknameAtAll(Player p, NicknameType... type);

    /**
     * 대상 플레이어에게 해당 타입들의 이름이 모두 존재하는지 조회합니다.
     *
     * @param u    대상 플레이어
     * @param type 대상 타입
     * @return 대상 플레이어의 지정한 타입의 닉네임이 모두 존재하는지의 여부
     */
    boolean hasNicknameAtAll(UUID u, NicknameType... type);

    /**
     * 대상 플레이어의 모든 타입 닉네임을 변경합니다.
     * 이는 {@link NicknameApi#setNickname(Player, String, NicknameType...)} )}에서 모든 파라미터를 가지고 호출한 결과와 동일합니다.
     *
     * @param p    대상 플레이어
     * @param name 닉네임
     */
    void setNickname(Player p, String name);

    /**
     * 대상 플레이어의 모든 타입 닉네임을 변경합니다.
     * 이는 {@link NicknameApi#setNickname(UUID, String, NicknameType...)} )}에서 모든 파라미터를 가지고 호출한 결과와 동일합니다.
     *
     * @param u    대상 플레이어
     * @param name 닉네임
     */
    void setNickname(UUID u, String name);

    /**
     * 대상 플레이어의 지정한 타입 닉네임을 변경합니다.
     *
     * @param p    대상 플레이어
     * @param name 닉네임
     * @param type 설정할 닉네임 타입
     */
    void setNickname(Player p, String name, NicknameType... type);

    /**
     * 대상 플레이어의 지정한 타입 닉네임을 변경합니다.
     *
     * @param u    대상 플레이어
     * @param name 닉네임
     * @param type 설정할 닉네임 타입
     */
    void setNickname(UUID u, String name, NicknameType... type);

    /**
     * 대상 플레이어의 닉네임을 가져옵니다.
     * 해당 메서드는 널 타입을 반환할 수 있습니다.
     *
     * @param p    대상 플레이어
     * @param type 닉네임 타입
     * @return 대상 플레이어의 지정된 타입에 설정된 닉네임
     */
    @Nullable String getNickname(Player p, NicknameType type);

    /**
     * 대상 플레이어의 닉네임을 가져옵니다.
     * 해당 메서드는 널 타입을 반환할 수 있습니다.
     *
     * @param u    대상 플레이어
     * @param type 닉네임 타입
     * @return 대상 플레이어의 지정된 타입에 설정된 닉네임
     */
    @Nullable String getNickname(UUID u, NicknameType type);

    /**
     * 현재 NicknameApi가 사용중인 스코어보드 데이터를 반환합니다.
     *
     * @return NicknameApi 스코어보드
     */
    Scoreboard getOccupiedScoreboard();

    /**
     * 대상 플레이어에게 현재 닉네임을 강제로 적용합니다.
     *
     * @param p 대상 플레이어
     */
    void updatePlayer(Player p);

    /**
     * 대상 플레이어에게 적용된 닉네임을 강제로 해제합니다.
     *
     * @param p 대상 플레이어
     */
    void resetPlayer(Player p);
}
