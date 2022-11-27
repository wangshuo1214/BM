package com.ws.bm.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ws.bm.common.constant.HttpStatus;
import com.ws.bm.common.utils.JwtTokenUtil;
import com.ws.bm.common.utils.MessageUtil;
import com.ws.bm.common.utils.RedisUtil;
import com.ws.bm.exception.BaseException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 全局的请求拦截器
 */
@Component
public class GlobalInterceptor implements HandlerInterceptor {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 前置处理：
     * 在业务处理器处理请求之前被调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            //在请求头中获取token
            String token = request.getHeader("Authorization");

            if (StrUtil.isEmpty(token)){
                //请求中不携带token，需要登录
                throw new BaseException(HttpStatus.UNAUTHORIZED, MessageUtil.getMessage("bm.loginTimeOut"));
            }
            Claims claims = jwtTokenUtil.getClaimsFromToken(token);
            String userId =(String) claims.get("userId");

            String cacheToken = redisUtil.getCacheObject("bmUserToken:" + userId);
            if (ObjectUtil.isEmpty(cacheToken)){
                //token在redis中已经过期，需要重新登录
                throw new BaseException(HttpStatus.UNAUTHORIZED, MessageUtil.getMessage("bm.loginTimeOut"));
            }
            if (!StrUtil.equals(token,cacheToken)){
                //请求token与缓存的token不一致，需要重新登录
                throw new BaseException(HttpStatus.UNAUTHORIZED, MessageUtil.getMessage("bm.loginTimeOut"));
            }
            //重新刷新token在缓存中的时间，实现长时间未登录就会重新登录，一直点击就不会重新登录
            redisUtil.expire("bmUserToken:"+userId,5, TimeUnit.HOURS);


        }catch (Exception e){
            //toekn解析失败或过期
            throw new BaseException(HttpStatus.UNAUTHORIZED, MessageUtil.getMessage("bm.loginTimeOut"));
        }

        return true;
    }

    /**
     * 中置处理：
     * 在业务处理器处理请求执行完成后，生成视图之前执行。
     * 后处理（调用了Service并返回ModelAndView，但未进行页面渲染），
     * 有机会修改ModelAndView ，现在这个很少使用了
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
    /**
     * 后置处理：
     * 在DispatcherServlet完全处理完请求后被调用，
     * 可用于清理资源等
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
