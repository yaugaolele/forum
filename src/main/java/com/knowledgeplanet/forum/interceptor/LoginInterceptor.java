package com.knowledgeplanet.forum.interceptor;

import com.knowledgeplanet.forum.config.AppConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Component
public class LoginInterceptor implements HandlerInterceptor {

    // 从配置文件中获取默认登录页的URL
    @Value("${bit-forum.login.url}")
    private String defaultURL;

    /**
     * 预处理(请求的前置处理)回调方法<br/>
     * 返回值: <br/>true 流程继续;<br/>
     *        false流程中断, 不会再调用其他的拦截器
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取用户session, 并判断用户是否登录
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(AppConfig.USER_SESSION) != null) {
            // 用户已登录
            return true;
        }
        // 没有登录强制跳转到登录页面
        if (!defaultURL.startsWith("/")) {
            defaultURL = "/" + defaultURL;
        }
        response.sendRedirect(defaultURL);
        return false;
    }
}
