package com.knowledgeplanet.forum.common;

import com.fasterxml.jackson.annotation.JsonInclude;


public class AppResult<T> {
    // 状态码
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private long code;
    // 状态描述
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String message;
    // 结果数据
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private T data;

    // 构造方法
    public AppResult() { }

    public AppResult(long code, String message) {
        this(code, message, null);
    }

    public AppResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ================= 封装常用方法 ====================
    /**
     * 成功
     */
    public static AppResult success() {
        return new AppResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    /**
     * 成功
     *
     * @param data 返回的结果
     */
    public static <T> AppResult<T> success(T data) {
        return new AppResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功
     *
     * @param message 自定义描述
     * @param data 返回的结果
     */
    public static <T> AppResult<T> success(String message, T data) {
        return new AppResult(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败
     */
    public static AppResult failed() {
        return new AppResult(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage());
    }

    /**
     * 失败
     *
     * @param message 自定义描述
     */
    public static <T> AppResult<T> failed(String message) {
        return new AppResult(ResultCode.FAILED.getCode(), message);
    }

    /**
     * 失败
     *
     * @param data 异常信息
     */
    public static <T> AppResult<T> failed(T data) {
        return new AppResult(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage(), data);
    }

    /**
     * 失败
     *
     * @param resultCode 错误状态
     */
    public static AppResult failed(ResultCode resultCode) {
        return new AppResult(resultCode.getCode(), resultCode.getMessage());
    }


    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
