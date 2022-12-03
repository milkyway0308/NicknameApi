package skywolf46.nicknameapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 해당 메서드가 스레드-안전함을 알립니다.
 * 다른 말로 하면, 해당 라이브러리에서 해당 어노테이션이 존재하지 않는 메서드는 해당 메서드가 실행되는
 * 주 스레드 바깥에서 실행되면 안됩니다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ThreadSafeMethod {
}
