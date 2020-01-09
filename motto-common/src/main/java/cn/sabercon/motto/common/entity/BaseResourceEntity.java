package cn.sabercon.motto.common.entity;

import cn.sabercon.motto.common.anno.NotCover;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 扩展的基础实体类，增加了用户 id 和删除标记
 *
 * @author ywk
 * @date 2019-10-15
 */
@Data
@MappedSuperclass
public abstract class BaseResourceEntity extends BaseEntity {

    /**
     * 用户 id
     */
    @NotCover
    @JsonIgnore
    @Column(nullable = false)
    private Long userId;

    /**
     * 删除标记：0-未删除，1-已删除
     */
    @NotCover
    @JsonIgnore
    private Integer del;
}
