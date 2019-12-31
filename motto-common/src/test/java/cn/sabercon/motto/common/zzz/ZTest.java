package cn.sabercon.motto.common.zzz;

import org.junit.jupiter.api.Test;

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
        Boy boy = new Boy();
        boy.setTestEnum(TestEnum.A);
        System.out.println(boy.getTestEnum().toString());
        Arrays.stream(boy.getTestEnum().getClass().getDeclaredFields()).forEach(e -> System.out.println(e.getName()));
    }
}
