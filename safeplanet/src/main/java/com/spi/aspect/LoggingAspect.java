/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAspect {
	private static final Logger LOG = LoggerFactory.getLogger(LoggingAspect.class);
	
//	@Before("execution(* com.spi.resource.*.*(..))")
//	public void logBefore(JoinPoint joinPoint) {
//
//		StringBuffer logMessage = new StringBuffer();
//		logMessage.append(joinPoint.getTarget().getClass().getName());
//		logMessage.append(".");
//		logMessage.append(joinPoint.getSignature().getName());
//		logMessage.append("(");
//		// append args
//		Object[] args = joinPoint.getArgs();
//		for (int i = 0; i < args.length; i++) {
//			logMessage.append(args[i]).append(",");
//		}
//
//		if (args.length > 0) {
//			logMessage.deleteCharAt(logMessage.length() - 1);
//		}
//
//		logMessage.append(")");
//		LOG.error(logMessage.toString());
//	}

	@AfterThrowing(pointcut = "execution(* com.spi.resource.*.*(..))", throwing = "error")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {

		StringBuffer logMessage = new StringBuffer();
		logMessage.append(joinPoint.getTarget().getClass().getName());
		logMessage.append(".");
		logMessage.append(joinPoint.getSignature().getName());
		logMessage.append("(");
		// append args
		Object[] args = joinPoint.getArgs();
		for (int i = 0; i < args.length; i++) {
			logMessage.append(args[i]).append(",");
		}

		if (args.length > 0) {
			logMessage.deleteCharAt(logMessage.length() - 1);
		}

		logMessage.append(")");
		logMessage.append(" Error :: " + error);
		error.printStackTrace();
		LOG.error(logMessage.toString());
	}

	@Around("execution(* com.spi.service.impl.*.*(..))")
	public Object logBeforeMethodAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		Object retVal = joinPoint.proceed();

		stopWatch.stop();

		StringBuffer logMessage = new StringBuffer();
		logMessage.append(joinPoint.getTarget().getClass().getName());
		logMessage.append(".");
		logMessage.append(joinPoint.getSignature().getName());
		logMessage.append("(");
		// append args
		Object[] args = joinPoint.getArgs();
		for (int i = 0; i < args.length; i++) {
			logMessage.append(args[i]).append(",");
		}
		if (args.length > 0) {
			logMessage.deleteCharAt(logMessage.length() - 1);
		}

		logMessage.append(")");
		logMessage.append(" Around:: execution time: ");
		logMessage.append(stopWatch.getTotalTimeMillis());
		logMessage.append(" ms");
		if(stopWatch.getTotalTimeMillis() > 100) LOG.info(logMessage.toString());
		
		return retVal;
	}

}
