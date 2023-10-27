package com.example.web_test.filter;

import com.example.web_test.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//@WebFilter(urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        //获取url
        String url = req.getRequestURI();

        String[] urls = new String[] {"/login","/css/**","/fonts/**","/img/**","/js/**", "/register", "/header/**"};
        boolean check = check(urls, url);

        //放行登录和注册请求
        if(check) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String jwt = req.getHeader("token");

        //未检测到token
        if(!StringUtils.hasLength(jwt)) {
            res.getWriter().write("FALSE");
            //res.sendRedirect("/login");
            return;
        }

        //解析token
        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().write("FALSE");
            //res.sendRedirect("/login");
            return;
        }

        //请求合法，放行
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
