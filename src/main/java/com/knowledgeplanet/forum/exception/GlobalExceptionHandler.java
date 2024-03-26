package com.knowledgeplanet.forum.exception;

import com.knowledgeplanet.forum.common.AppResult;
import com.knowledgeplanet.forum.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


// 添加注解
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义的已知异常
     * @param e ApplicationException
     * @return AppResult
     */
    // 以Json形式返回
    @ResponseBody
    // 指定要处理的异常
    @ExceptionHandler(ApplicationException.class)
    public AppResult handleApplicationException (ApplicationException e) {
        // 打印异常
        e.printStackTrace();
        // 记录日志
        log.error(e.getMessage());
        // 获取异常信息
        if (e.getErrorResult() != null) {
            // 返回异常类中记录的状态
            return e.getErrorResult();
        }
        // 默认返回异常信息
        return AppResult.failed(e.getMessage());
    }

    /**
     * 处理所有未捕获的其他异常
     * @param e Exception
     * @return AppResult
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public AppResult handleException (Exception e) {
        // 打印异常
        e.printStackTrace();
        // 记录日志
        log.error(e.getMessage());
        if (e.getMessage() == null) {
            return AppResult.failed(ResultCode.ERROR_SERVICES);
        }
        // 默认返回异常信息
        return AppResult.failed(e.getMessage());
    }
}
