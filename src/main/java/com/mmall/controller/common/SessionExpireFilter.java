package com.mmall.controller.common;
/*
    author: king
    date: 2018/6/24
*/

import com.mmall.common.Const;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SessionExpireFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User user = JsonUtil.string2Obj(RedisPoolUtil.get(login_token), User.class);
            if (user != null){
                RedisPoolUtil.setEx(login_token, JsonUtil.obj2String(user), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
