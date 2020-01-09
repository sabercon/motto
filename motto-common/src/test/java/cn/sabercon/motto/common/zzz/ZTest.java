package cn.sabercon.motto.common.zzz;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

/**
 * 通用测试类
 *
 * @author ywk
 * @date 2019-12-20
 */
class ZTest {

    @Test
    void common() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(6);
        System.out.println(localDateTime);
        LocalDateTime dateTime = localDateTime.minusDays(2914);
        System.out.println(dateTime);
    }
}
