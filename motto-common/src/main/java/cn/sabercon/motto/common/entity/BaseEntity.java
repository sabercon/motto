package cn.sabercon.motto.common.entity;

import cn.sabercon.motto.common.anno.NotCover;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类，主键自增，自动维护创建时间和更新时间
 *
 * @author ywk
 * @date 2019-10-15
 */
@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    /**
     * 主键 id
     */
    @Id
    @NotCover
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @NotCover
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @NotCover
    @UpdateTimestamp
    private LocalDateTime updateTime;
}
