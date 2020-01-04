package cn.sabercon.motto.common.anno;

import java.lang.annotation.*;

/**
 * 标记一个实体类的属性，表示该属性不要被前端传递参数覆盖
 *
 * @see cn.sabercon.motto.common.util.EntityUtils
 * @author ywk
 * @date 2020-01-03
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotCover {
}
