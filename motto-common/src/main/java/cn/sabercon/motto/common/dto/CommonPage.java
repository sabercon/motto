package cn.sabercon.motto.common.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 通用分页对象
 *
 * @author ywk
 * @date 2019-10-15
 */
@Data
public class CommonPage<T> {

    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 当前页幅
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 总目数
     */
    private Long totalNum;
    /**
     * 当前页数据列表
     */
    private List<T> list;

    /**
     * 将SpringDataJpa分页后的list转为分页信息
     */
    public static <T> CommonPage<T> of(Page<T> pageInfo) {
        CommonPage<T> result = new CommonPage<T>();
        result.setPageNum(pageInfo.getNumber());
        result.setPageSize(pageInfo.getSize());
        result.setTotalPage(pageInfo.getTotalPages());
        result.setTotalNum(pageInfo.getTotalElements());
        result.setList(pageInfo.getContent());
        return result;
    }
}
