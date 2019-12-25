package cn.sabercon.motto.common.component;

import cn.sabercon.motto.common.exception.CommonException;
import cn.sabercon.motto.common.response.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 捕获异常的通知类
 *
 * @author ywk
 * @date 2019-10-15
 */
@RestControllerAdvice
@Slf4j
public class ExceptionCatcher {

    /**
     * 捕获自定义的{@link CommonException}
     */
    @ExceptionHandler(CommonException.class)
    public CommonResult commonExceptionHandler(CommonException e) {
        log.warn("catch CommonException:{}, code:{}, msg:{}", e.getErrorCode().name(), e.getErrorCode().getCode(), e.getErrorCode().getMsg());
        return CommonResult.fail(e.getErrorCode());
    }

    /**
     * 主要捕获{@link Assert}抛出的{@link IllegalArgumentException}
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public CommonResult illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.warn("catch IllegalArgumentException, msg:{}", e.getMessage());
        return CommonResult.fail(e.getMessage());
    }

    /**
     * 主要捕获{@link Assert}抛出的{@link IllegalStateException}
     */
    @ExceptionHandler(IllegalStateException.class)
    public CommonResult illegalStateExceptionHandler(IllegalStateException e) {
        log.warn("catch IllegalStateException, msg:{}", e.getMessage());
        return CommonResult.fail(e.getMessage());
    }
}
