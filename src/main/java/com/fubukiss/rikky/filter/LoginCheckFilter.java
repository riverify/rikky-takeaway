package com.fubukiss.rikky.filter;

import com.alibaba.fastjson.JSON;
import com.fubukiss.rikky.common.BaseContext;
import com.fubukiss.rikky.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>Project: rikky-takeaway - LoginCheckFilter 自定义过滤器，检查用户是否登录
 * <p>Powered by Riverify On 12-16-2022 22:59:51
 *
 * <p>通过 @WebFilter 注解，将过滤器注册到 Servlet 容器中。
 * <br>urlPatterns 属性指定过滤器拦截的请求路径。"/*" 表示拦截所有请求。
 * <br>filterName 属性指定过滤器的名称。
 * <p>通过 @Slf4j 注解，自动注入日志对象。
 *
 * <p>该过滤器的处理逻辑：<br>
 * 1.获取本次处理的url。<br>
 * 2.判断本次请求是否需要处理。<br>
 * 3.如果不需要处理，则直接放行。<br>
 * 4.判断登陆状态，如果为已登录，则放行。<br>
 * 5.如果未登录，则返回未登录结果。<br>
 * <p/>
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")  // "/*"表示拦截所有请求
@Slf4j
public class LoginCheckFilter implements Filter {

    // 路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // 将ServletRequest和ServletResponse转换成HttpServletRequest和HttpServletResponse
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1.获取本次处理的url
        String requestURI = request.getRequestURI();
        log.info("请求路径：{}", requestURI);                                        // Slf4j的日志输出

        // 2.判断本次请求是否需要处理
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/login",
                "/user/sendMsg"
        };                              // 不需要处理的url

        boolean needProcess = check(urls, requestURI);  // 判断本次请求是否需要处理

        // 3.如果不需要处理，则直接放行
        if (needProcess) {
            log.info("本次请求{}不需要处理，直接放行", requestURI);                     // Slf4j的日志输出
            filterChain.doFilter(request, response);    // 直接放行
            return;                                     // 结束方法
        }

        // 4-1.判断员工登陆状态，如果为已登录，则放行
        Object employeeId = request.getSession().getAttribute("employee");        // 获取session中的employee对象
        if (employeeId != null) {
            log.info("本次请求{}，用户id={}，已登录，直接放行", requestURI, employeeId);   // Slf4j的日志输出

            // 在该线程中保存当前用户的id，BaseContext为common包中的工具类，用于保存当前线程的数据
            BaseContext.setCurrentId((Long) employeeId);

            filterChain.doFilter(request, response);    // 放行
            return;                                     // 结束方法
        }

        // 4-2.判断用户登陆状态，如果为已登录，则放行
        if (request.getSession().getAttribute("user") != null) {
            log.info("本次请求{}，用户id={}，已登录，直接放行", requestURI, request.getSession().getAttribute("user"));   // Slf4j的日志输出

            Long userId = (Long) request.getSession().getAttribute("user");
            // 在该线程中保存当前用户的id，BaseContext为common包中的工具类，用于保存当前线程的数据
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response);    // 放行
            return;                                     // 结束方法
        }

        // 5.如果未登录，则返回未登录结果
        // 由于前端代码中引入了js/request.js，相应拦截器会帮我们跳转到登录页面，所以这里不需要跳转，只需要返回未登录结果即可
        // 即只要通过输出流,包装通用返回结果类，返回未登录结果即可
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));   // 通过输出流，返回未登录结果
        log.info("本次请求{}用户未登录，返回未登录结果", requestURI);                    // Slf4j的日志输出

    }


    /**
     * 路径匹配<br>
     * 判断本次请求是否需要处理。
     *
     * @param urls       不需要处理的url
     * @param requestURI 本次请求的url
     * @return true：本次请求的url匹配其中一个不需要处理的urls，不处理；false：否则需要处理
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            if (PATH_MATCHER.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }

}
