package cn.sabercon.motto.common.zzz;

import cn.sabercon.motto.common.anno.NotCover;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ywk
 * @date 2019-12-20
 */
@Data
@NoArgsConstructor
public class Boy {

    @NotCover
    private String name;
    private Integer age;
    private Girl girlFriend;
    private TestEnum testEnum;
}
