package com.avatar.configurations;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;


import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class AllAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException ex) throws IOException, ServletException {

		String jsonPayload = "{\"message\" : \"%s\", \"timestamp\" : \"%s\" }";
        response.getOutputStream().println(String.format(jsonPayload, ex.getMessage(), Calendar.getInstance().getTime()));
	}

}
