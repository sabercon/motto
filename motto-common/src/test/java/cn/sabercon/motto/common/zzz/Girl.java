package cn.sabercon.motto.common.zzz;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ywk
 * @date 2019-12-20
 */
@Data
@NoArgsConstructor
public class Girl {
    private String name;
    private Integer age;
    private Boy boyFriend;
}
