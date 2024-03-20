package org.englising.com.englisingbe.singleplay.controller;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class SinglePlayControllerLoggingAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* org.englising.com.englisingbe.singleplay.controller.*.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] parameterValues = joinPoint.getArgs();

        String className = method.getDeclaringClass().getSimpleName(); // 클래스 이름만
        Object[] args = joinPoint.getArgs();
        String arguments = Arrays.stream(args)
                .map(arg -> arg == null ? "null" : arg.toString())
                .collect(Collectors.joining(", "));

        logger.info("Start of method: {} in class: {} with arguments [{}]", method.getName(), className, arguments);

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        logger.info("End of method: {} in class: {} with duration: {}ms", method.getName(), className, (endTime - startTime));

        return result;
    }
}
