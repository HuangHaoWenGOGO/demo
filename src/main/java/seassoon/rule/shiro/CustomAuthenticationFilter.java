package seassoon.rule.shiro;

import seassoon.rule.exception.CustomExceptionMessage;
import seassoon.rule.exception.CustomExceptionResponse;
import seassoon.rule.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: 张前峰
 * @Date: 2019/6/26 13:50
 * @Description:
 */
@Slf4j
@Component
public class CustomAuthenticationFilter extends FormAuthenticationFilter {


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (this.isLoginRequest(request, response)) {
            if (this.isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }

                return this.executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }

                return true;
            }
        } else {

            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the " +
                        "Authentication url [" + getLoginUrl() + "]");
            }

            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

//            if (isAjax(httpServletRequest)) {

            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");


            CustomExceptionMessage message = CustomExceptionMessage
                    .valueOf(Integer.valueOf(CustomExceptionMessage.USER_AUTHENTICATION.getCode()));

            CustomExceptionResponse body = new CustomExceptionResponse(
                    HttpStatus.UNAUTHORIZED, message, httpServletRequest.getRequestURI());

            httpServletResponse.getWriter().write(JsonUtil.toString(body));

            return false;
//            } else {
//
//
//                saveRequestAndRedirectToLogin(request, response);
//                return false;
//            }


        }

    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        return super.onLoginFailure(token, e, request, response);
    }

    /**
     * 判断ajax请求
     *
     * @param request
     * @return
     */
    boolean isAjax(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null && "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()));
    }
}
