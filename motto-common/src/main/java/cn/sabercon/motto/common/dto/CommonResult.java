package cn.sabercon.motto.common.dto;

import cn.sabercon.motto.common.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用返回对象
 *
 * @author ywk
 * @date 2019-10-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> {

    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误信息
     */
    private String msg;
    /**
     * 返回数据
     */
    private T data;

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(true, null, null, data);
    }

    public static CommonResult success() {
        return success(null);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     */
    public static <T> CommonResult<T> fail(ErrorCode errorCode) {
        return new CommonResult<T>(false, errorCode.getCode(), errorCode.getMsg(), null);
    }

    public static <T> CommonResult<T> fail(String msg) {
        return new CommonResult<T>(false, null, msg, null);
    }

    public static <T> CommonResult<T> fail() {
        return fail(ErrorCode.FAIL);
    }
}
