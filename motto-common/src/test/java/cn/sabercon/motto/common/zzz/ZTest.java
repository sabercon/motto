package cn.sabercon.motto.common.zzz;

import cn.sabercon.motto.common.util.EntityUtils;
import cn.sabercon.motto.common.util.NameUtils;
import com.oracle.webservices.internal.api.message.ContentType;
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
        String awdadad = NameUtils.getOssName("awdadad");
        System.out.println(awdadad);
        System.out.println(awdadad.length());
    }
}
