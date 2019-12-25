package cn.sabercon.motto.common.exception;

import cn.sabercon.motto.common.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用的异常类
 *
 * @author ywk
 * @date 2019-10-15
 */
@AllArgsConstructor
@Getter
public class CommonException extends RuntimeException {

    private ErrorCode errorCode;
}
