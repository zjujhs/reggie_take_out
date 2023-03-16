package com.jjjhs.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.jjjhs.reggie.common.BaseContext;
import com.jjjhs.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经完成登录
 */

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURL = request.getRequestURI();

        // 不需要过滤的请求路径
        String[] urls = new String[] {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/common/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };

        // 判断是否处理
        if(check(urls, requestURL)) {
            //  无需处理
            filterChain.doFilter(request, response);
            return;
        }

        Long empId = (Long) request.getSession().getAttribute("employee");
        if(empId != null) {
            BaseContext.setThreadId(empId);
            filterChain.doFilter(request, response);
            return;
        }

        Long userId = (Long) request.getSession().getAttribute("user");
        if(userId != null) {
            BaseContext.setThreadId(userId);
            filterChain.doFilter(request, response);
            return;
        }

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    public boolean check (String[] urls, String requestURL) {
        for(String url: urls) {
            boolean match = PATH_MATCHER.match(url, requestURL);
            if(match) return true;
        }
        return false;
    }
}
