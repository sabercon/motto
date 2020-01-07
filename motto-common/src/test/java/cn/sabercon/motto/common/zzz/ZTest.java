package cn.sabercon.motto.common.zzz;

import cn.sabercon.motto.common.util.EntityUtils;
import cn.sabercon.motto.common.util.NameUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

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
