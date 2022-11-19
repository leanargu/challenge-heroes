package com.leandro.heroesapi.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
public class LoggingAspect {

    @Autowired(required = false)
    private HttpServletRequest request;
    @Autowired(required = false)
    private HttpServletResponse response;
    public static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("@annotation(com.leandro.heroesapi.aspect.LogExecutionTime)")
    public Object methodTimeLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        request.getRequestURL();
        String httpMethod = request.getMethod();
        String requestUri = request.getRequestURI();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        logExecutionTime(httpMethod, requestUri, stopWatch.getTotalTimeMillis());
        return result;
    }

    private static void logExecutionTime(String httpMethod, String requestUri, double totalTimeMillis) {
        if (logger.isInfoEnabled()) {
            logger.info("{}: '{}' - elapsed time: {} ms.",
                    httpMethod,
                    requestUri,
                    totalTimeMillis);
        }
    }
}
