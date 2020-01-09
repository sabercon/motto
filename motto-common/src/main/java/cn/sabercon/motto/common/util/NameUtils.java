package cn.sabercon.motto.common.util;

import cn.hutool.core.util.IdUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 处理名称的工具类
 *
 * @author ywk
 * @date 2019-12-30
 */
@Slf4j
@UtilityClass
public class NameUtils {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/");

    /**
     * 在文件名前面加上UUID和时间目录
     *
     * @param filename 初始文件名称
     * @return 上传 oss 的文件路径
     */
    public String getOssName(String filename) {
        String dir = formatter.format(LocalDateTime.now());
        String prefix = IdUtil.fastSimpleUUID() + "-";
        return dir + prefix + filename;
    }

    /**
     * 获取数据库模糊查询的名称
     *
     * @param name 初始名称，可为空
     * @return 可用于模糊查询的名称
     */
    public String getLikeName(String name) {
        if (name == null) {
            name = "";
        }
        return "%" + name.trim() + "%";
    }
}
