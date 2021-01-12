package seassoon.rule.aop;


import seassoon.rule.utils.JsonUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志切面类
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    /**
     * ..表示包及子包 该方法代表controller层的所有方法  TODO 路径需要根据自己项目定义
     */
    @Pointcut("execution(public * seassoon.rule.controller..*.*(..))")
    public void controllerMethod() {
    }


    /**
     * 方法执行前
     *
     * @param joinPoint
     * @throws Exception
     */
    @Before("controllerMethod()")
    public void logRequestInfo(JoinPoint joinPoint) throws Exception {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        StringBuilder requestLog = new StringBuilder();
        Signature signature = joinPoint.getSignature();

        String methodComments = ((MethodSignature) signature).getMethod().getAnnotation(ApiOperation.class) != null ?
                ((MethodSignature) signature).getMethod().getAnnotation(ApiOperation.class).value() : ((MethodSignature) signature).getMethod().getName();

        requestLog.append(methodComments).append("\t")
                .append("请求信息：").append("URL = {").append(request.getRequestURI()).append("},\t")
                .append("请求方式 = {").append(request.getMethod()).append("},\t")
                .append("请求IP = {").append(request.getRemoteAddr()).append("},\t")
                .append("类方法 = {").append(signature.getDeclaringTypeName()).append(".")
                .append(signature.getName()).append("},\t");

        

        log.info(requestLog.toString());
    }


    /**
     * 方法执行后
     *
     * @param result
     * @throws Exception
     */
    @AfterReturning(returning = "result", pointcut = "controllerMethod()")
    public void logResultInfo(Object result) throws Exception {
        log.info("请求结果：" + JsonUtil.toString(result));
    }

}
