package cn.sabercon.motto.common.dto;

import cn.sabercon.motto.common.util.NameUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

/**
 * 分页请求的实体类
 * 注：排序和查询暂时只支持一项
 *
 * @author ywk
 * @date 2020-01-02
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageReq {

    Integer pageNum;

    Integer pageSize;

    /**
     * 精确查询的值
     */
    String equalValue;

    /**
     * 模糊查询的值
     */
    String fuzzyValue;

    /**
     * 要排序的属性
     */
    String sortField;

    /**
     * 排序方向
     */
    Sort.Direction direction;

    /**
     * 分页查询信息
     */
    Pageable pageable;

    /**
     * 将 fuzzyField 转为可直接模糊查询的值
     */
    public void amendFuzzy() {
        fuzzyValue = NameUtils.getLikeName(fuzzyValue);
    }

    /**
     * 如果排序属性为空，则默认为创建时间
     */
    public void amendSort() {
        if (StringUtils.isEmpty(sortField)) {
            sortField = "createDate";
        }
    }

    /**
     * 如果排序方向为空，则默认为升序
     */
    public void amendDirection() {
        if (direction == null) {
            direction = Sort.DEFAULT_DIRECTION;
        }
    }

    /**
     * 默认页码为0
     */
    public void amendPageNum() {
        if (pageNum == null || pageNum < 0) {
            pageNum = 0;
        }
    }

    /**
     * 默认页幅为10
     */
    public void amendPageSize() {
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
    }

    public void createPageable() {
        Sort sort = Sort.by(direction, sortField);
        pageable = PageRequest.of(pageNum, pageSize, sort);
    }

    /**
     * 调用所有 amend 方法 和 create 方法
     */
    public void amendAll() {
        amendFuzzy();
        amendSort();
        amendDirection();
        amendPageNum();
        amendPageSize();
        createPageable();
    }
}
