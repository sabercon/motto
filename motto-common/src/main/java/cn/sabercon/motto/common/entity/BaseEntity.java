package cn.sabercon.motto.common.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类，主键自增，自动维护创建时间和更新时间
 *
 * @author ywk
 * @date 2019-10-15
 */
@MappedSuperclass
@Data
public abstract class BaseEntity implements Serializable {

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDate;



    /**
     * 更新时间
     */
    @UpdateTimestamp
    private LocalDateTime updateDate;
}
