package com.ealanta;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class LoggingInterceptor extends HandlerInterceptorAdapter {
	
	private Log LOG = LogFactory.getLog(LoggingInterceptor.class);

	private static ThreadLocal<String> INVOCATION = new ThreadLocal<>();
	private static ThreadLocal<Long> TL = new ThreadLocal<>();
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		TL.set(System.currentTimeMillis());
		String invocation = String.format("[%s %s]", request.getMethod(), getFullURL(request));
		INVOCATION.set(invocation);
		return true;
	}


	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		
		long diff = System.currentTimeMillis() - TL.get();
		
		LOG.info(String.format(" %s took[%s]ms",getInvocation(),diff));
		TL.set(null);
		INVOCATION.set(null);
	}
	
	public static String getFullURL(ServletRequest req) {
		HttpServletRequest request = (HttpServletRequest)req;
	    StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
	    String queryString = request.getQueryString();

	    if (queryString == null) {
	        return requestURL.toString();
	    } else {
	        return requestURL.append('?').append(queryString).toString();
	    }
	}
	
	public static String getInvocation() {
		return INVOCATION.get();
	}
	
}