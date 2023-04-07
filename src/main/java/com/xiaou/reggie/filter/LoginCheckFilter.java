package com.xiaou.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.xiaou.reggie.common.BaseContext;
import com.xiaou.reggie.common.R;
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
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER =new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest) servletRequest;
        HttpServletResponse rsp=(HttpServletResponse) servletResponse;


        //1.获得本次请求的uri
        String requestURL = req.getRequestURI();


        log.info("拦截到请求{}",requestURL);
        //定义不需要处理的请求路径
        String[] urls=new String[] {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };
        //2.判断本次请求是否需要处理
        boolean check = check(urls, requestURL);

        //3.如果不需要处理，则直接放行
        if (check) {
            filterChain.doFilter(req,rsp);
            log.info("本次请求{}不需要处理",requestURL);
            return;
        }
        //4.判断登录状态，如果已登录，则直接放行
        Object employee = req.getSession().getAttribute("employee");


        if (employee!=null){
            log.info("用户已经登录 用户ID为{}",req.getSession().getAttribute("employee"));
            Long empId=(Long) req.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(req, rsp);
            return;
        }

        //5.如果未登录返回未登录的结果,通过输出流方式，向客户端页面响应数据
        rsp.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param requestURI
     * @param urls
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            if (PATH_MATCHER.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
