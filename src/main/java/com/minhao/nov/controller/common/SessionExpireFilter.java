package com.minhao.nov.controller.common;

import com.minhao.nov.common.Const;
import com.minhao.nov.pojo.MmallUser;
import com.minhao.nov.util.CookieUtil;
import com.minhao.nov.util.JsonUtil;
import com.minhao.nov.util.RedisPoolUtil;
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

        HttpServletRequest httpServletRequest=(HttpServletRequest) servletRequest;

        String loginToken= CookieUtil.readToken(httpServletRequest);
        if (StringUtils.isNoneEmpty(loginToken)){
            String s = RedisPoolUtil.get(loginToken);
            MmallUser user= JsonUtil.String2Obj(s,MmallUser.class);
            if (user!=null){
                RedisPoolUtil.expire(s, Const.RedisCachetime.SESSION_TIME);

            }
        }

        filterChain.doFilter(servletRequest,servletResponse);



    }

    @Override
    public void destroy() {

    }
}
