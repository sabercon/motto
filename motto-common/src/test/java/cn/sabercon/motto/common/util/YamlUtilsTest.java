package cn.sabercon.motto.common.util;

import org.junit.jupiter.api.Test;

import java.util.Map;

class YamlUtilsTest {

    @Test
    void parse() {
        Map<String, Object> map = YamlUtils.parse("test");
        System.out.println(map);
    }
}
