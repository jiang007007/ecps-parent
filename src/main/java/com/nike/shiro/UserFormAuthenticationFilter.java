package com.nike.shiro;

import com.nike.entity.ActiveUser;
import com.nike.entity.User;
import com.nike.service.UserService;
import com.nike.utils.WebUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义一个表单过滤器的目的就是认证流程由自己控制
 *
 */
public class UserFormAuthenticationFilter extends FormAuthenticationFilter {
    @Autowired
    private UserService service;

    /**
     *      只要请求地址不是post请求和不是user/login  那么就返回登录页面
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //记住我 就实现自动登录
        if (autoLogin(httpRequest)){
            return  true;
        }


        //判断是否登录页面地址请求地址，如果不是那么重顶向到controller的方法中
        if (isLoginRequest(request,response)){
            //请求是 http post
            if (isLoginSubmission(request,response)){
                //在提交给realm查询前，先判断验证码
                if (WebUtils.validateCaptcha(httpRequest)){
                    return executeLogin(request,response);
                }else {
                    if (isAjax(httpRequest)){
                        WebUtils.printCNJSON("{\"message\":\"验证码错误\"}",httpServletResponse);
                        return false;
                    }else {
                        httpRequest.setAttribute("shiroLoginFailure","captchaCodeError");
                        return true;
                    }
                }
            }else {
                //不是post 放行
                return true;
            }
        }else {
            if (isAjax(httpRequest)){
                return false;
            }else {
                //返回配置的user/login.do 该方法会重定向到登录页面，再次发送请求给本方法
                saveRequestAndRedirectToLogin(request, response);
            }
            return false;
        }

    }

    //判断是否为ajax请求
    private boolean isAjax(HttpServletRequest httpRequest) {
        return (httpRequest.getHeader("X-Requested-With")!= null &&"XMLHttpRequest".equals(httpRequest.getHeader("X-Request-With")));
    }


    /**
     *  用户要是在登录的时候点击了“记住我" 那么实现自动登录
     * @param httpRequest
     * @return
     */
    private boolean autoLogin(HttpServletRequest httpRequest) {
        //subject 主体（用户  程序等 进行认证的都称为主体）
        Subject currentUser = SecurityUtils.getSubject();
        //如果 isAuthenticated 为false 证明不是登录过的 同时isRemember为true  证明是没有登录直接通过记住我功能进来的
        if (!currentUser.isAuthenticated() && currentUser.isRemembered()){
            //Principal 身份信息（标识必须具有唯一性，e.g 用户名 手机号 邮箱地址等 一个主体可以有多个身份，但必须有一个主身份（Primary Principal））
            ActiveUser activeUser =(ActiveUser)SecurityUtils.getSubject().getPrincipal();
            //获取到session 看看是否为空
            Session session = currentUser.getSession();
            if (session.getAttribute("currentUser") == null){
                User user = service.validateUserExist(activeUser.getUserEmail());
                UsernamePasswordToken token = new UsernamePasswordToken(user.getUserEmail(), user.getUserPassword(), currentUser.isRemembered());
                //吧当前用户放入session
                currentUser.login(token);
                session.setAttribute("currentUser", user);
                //设置会话的过期时间--ms,默认是30分钟，设置负数表示永不过期
                session.setTimeout(-1000l);

                //这是httpSession、用户页面获取数据的。
                httpRequest.getSession().setAttribute("activeUser", activeUser);

                return true;
            }
        }
        return false;
    }

    /**
     * 认证成功，把用户认真信息保存在session中，盘带你是否为ajax请求
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     */

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        return super.onLoginSuccess(token, subject, request, response);
    }
}
