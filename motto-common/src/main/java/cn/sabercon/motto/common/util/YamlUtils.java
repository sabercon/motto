package cn.sabercon.motto.common.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

/**
 * 解析yml文件的工具类
 *
 * @author ywk
 * @date 2019-11-15
 */
@UtilityClass
public class YamlUtils {

    /**
     * 解析类路径下的yml或yaml文件
     * 优先解析后缀名为yml的文件
     *
     * @param fileName 文件名，不含后缀时会自动补上
     * @return yml文件解析后的结果
     */
    public <T> T parse(String fileName) {
        Yaml yaml = new Yaml();
        InputStream inputStream;
        if (fileName.endsWith(".yml") || fileName.endsWith(".yaml")) {
            inputStream = YamlUtils.class.getClassLoader().getResourceAsStream(fileName);
        } else {
            inputStream = YamlUtils.class.getClassLoader().getResourceAsStream(fileName + ".yml");
            if (inputStream == null) {
                inputStream = YamlUtils.class.getClassLoader().getResourceAsStream(fileName + ".yaml");
            }
        }
        Assert.notNull(inputStream, "yml or yaml file not found");
        return yaml.load(inputStream);
    }
}
