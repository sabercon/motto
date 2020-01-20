package cn.sabercon.motto.common.dto;

import cn.sabercon.motto.common.util.NameUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * 分页请求的实体类
 * 注：排序和查询暂时只支持一项
 * 更复杂的查询请使用 {@link JpaSpecificationExecutor}
 *
 * @author ywk
 * @date 2020-01-02
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageReq implements Serializable {

    private static final long serialVersionUID = -3506120969187425400L;

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
            sortField = "createTime";
        }
    }

    /**
     * 如果排序方向为空，则默认为升序
     */
    public void amendDirection() {
        if (direction == null) {
            direction = Sort.Direction.DESC;
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

    /**
     * 调用所有 amend 方法 和 create 方法
     */
    public void amendAll() {
        amendFuzzy();
        amendSort();
        amendDirection();
        amendPageNum();
        amendPageSize();
    }

    /**
     * 生成用于 jpa 分页查询的 {@link Pageable}
     * 默认会最后按id排序以防止查询结果不一致
     */
    public Pageable toPageable() {
        Sort sort = Sort.by(direction, sortField);
        return PageRequest.of(pageNum, pageSize, sort.and(Sort.by(Sort.Direction.DESC, "id")));
    }
}
