package com.ealanta;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jboss.logging.Logger;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class TimingAspect {

	private static final Logger LOG = Logger.getLogger(TimingAspect.class);
	
	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	private void webEndpoints(){}
	   
	@Around("webEndpoints()")
	public Object timeMethod(ProceedingJoinPoint pjp) throws Throwable {
		long start = System.currentTimeMillis();
		try {
			return pjp.proceed();
		}finally {
			long diff = System.currentTimeMillis() - start;
			String invoke = LoggingInterceptor.getInvocation();
			LOG.info(String.format("%s[%s]took[%s]ms",invoke,pjp.getSignature(),diff));
		}
	}
}
