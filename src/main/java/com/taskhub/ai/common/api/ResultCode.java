package com.taskhub.ai.common.api;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS("00000","操作成功"),

    // A 开头代表客户端错误（前端参数传错了，或者没登陆）
    USER_ERROR("A0001","用户端错误"),
    PARAM_ERROR("A0400","请求参数错误"),
    UNAUTHORIZED("A0401","没有登录或者 Token 已过期"),
    FORBIDDEN("A0403","权限不足，拒绝访问"),

    // B 开头代表服务端内部错误（业务大脑错误）
    SYSTEM_ERROR("B0001","系统执行出错"),
    BIZ_ERROR("B0002","业务处理异常");

    private final String code;
    private final String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}