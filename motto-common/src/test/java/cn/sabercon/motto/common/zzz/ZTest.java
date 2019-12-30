package cn.sabercon.motto.common.zzz;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 通用测试类
 *
 * @author ywk
 * @date 2019-12-20
 */
class ZTest {

    @Test
    void common() {
        LocalDateTime now = LocalDateTime.now();
        String prefix = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        System.out.println(prefix);
    }
}
