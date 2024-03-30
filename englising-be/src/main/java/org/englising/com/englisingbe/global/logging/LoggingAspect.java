package org.englising.com.englisingbe.global.logging;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
public class LoggingAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* org.englising.com.englisingbe.*.*.*.*(..)) " +
            "&& !execution(* org.englising.com.englisingbe.auth.jwt.JwtAuthenticationFilter.*(..))" +
            "&& !execution(* org.englising.com.englisingbe.auth.jwt.JwtExceptionFilter.*(..)))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName();
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();
        StringBuilder params = new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) params.append(", ");
            params.append(parameters[i].getName()).append(": ").append(args[i] == null ? "null" : args[i].toString());
        }
        logger.info("{} -> Starting Method [{}], Parameters [{}]", className, methodName, params);

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        logger.info("{} -> End of Method [{}] with duration: {}ms", className, methodName, (endTime - startTime));
        return result;
    }
}
