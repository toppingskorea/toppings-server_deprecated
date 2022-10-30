package com.toppings.server.domain_global.config.aop;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.game_studio.pr.common.utils.request.RequestIpProvider;

@Component
@Aspect
public class RequestLogAspect {

	private final Logger logger = LoggerFactory.getLogger(RequestLogAspect.class);

	@Pointcut("onServerController()")
	public void onRequest() {
	}

	@Pointcut("execution(* com.game_studio.pr.server.domain.*.controller.*.*(..))")
	public void onServerController() {
	}

	@Pointcut("onServerService()")
	public void onService() {
	}

	@Pointcut("execution(* com.game_studio.pr.server.domain.*.service.*.*(..))")
	public void onServerService() {
	}

	@Before("onRequest()")
	public void beforeParameterLog(JoinPoint joinPoint) {
		HttpServletRequest request
			= ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		logger.debug("============================= Request Info Start ==============================");
		logger.debug("Request: {} - {} {}", getTimeStamp(), RequestIpProvider.getIp(request), request.getRequestURI());
		logger.debug("{} - {}", getDeclaringTypeName(joinPoint), getMethod(joinPoint).getName());

		Object[] args = joinPoint.getArgs();
		if (args == null || args.length <= 0) {
			logger.debug("No Parameter");
		} else {
			for (Object arg : args) {
				logger.debug("Parameter Value: {}", arg);
			}
		}
		logger.debug("============================== Request Info End =============================");
	}

	@Before("onService()")
	public void beforeServiceLog(JoinPoint joinPoint) {
		logger.debug("============================= Service Start ==============================");
	}

	@After("onService()")
	public void afterServiceLog(JoinPoint joinPoint) {
		logger.debug("============================= Service End ==============================");
	}

	private String getTimeStamp() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Timestamp(System.currentTimeMillis()));
	}

	private String getDeclaringTypeName(JoinPoint joinPoint) {
		return joinPoint.getSignature().getDeclaringTypeName();
	}

	private Method getMethod(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		return signature.getMethod();
	}
}
