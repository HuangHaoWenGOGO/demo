package seassoon.rule.shiro;

import seassoon.rule.exception.CustomExceptionMessage;
import seassoon.rule.exception.CustomExceptionResponse;
import seassoon.rule.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JwtFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        try {
            executeLogin(request, response);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return false;
        }
    }

    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("Authorization");
        System.out.println("----executeLogin------");
        if (token == null) {
            log.info("token为空");
            throw new Exception("token 为空");
        } else {
            JwtToken jwtToken = new JwtToken(token);
            // 提交给realm进行登入，如果错误他会抛出异常并被捕获
//            try {
            getSubject(request, response).login(jwtToken);
//            } catch (AuthenticationException e) {
//                e.printStackTrace();
//                return false;
//            }
            return true;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        if (log.isTraceEnabled()) {
            log.trace("Attempting to access a path which requires authentication.  Forwarding to the Authentication url [" + this.getLoginUrl() + "]");
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");


        CustomExceptionMessage message = CustomExceptionMessage
                .valueOf(Integer.valueOf(CustomExceptionMessage.TOKEN_ERROR.getCode()));

        CustomExceptionResponse body = new CustomExceptionResponse(
                HttpStatus.UNAUTHORIZED, message, httpServletRequest.getRequestURI());

        httpServletResponse.getWriter().write(JsonUtil.toString(body));

//			throw new AuthenticationException("未登录");

        return false;


    }
}
