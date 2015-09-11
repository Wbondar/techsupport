package pl.chelm.pwsz.techsupport.web;

import java.util.ResourceBundle;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.chelm.pwsz.techsupport.domain.Action;
import pl.chelm.pwsz.techsupport.domain.Member;

public final class Authenticator
extends Object
implements Filter
{
	@Override
	public void init (FilterConfig config)
	throws ServletException
	{

	}

	@Override
	public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
	throws ServletException, IOException
	{
		HttpSession session = ((HttpServletRequest)request).getSession(false);
		Member user = null;
		if (session != null)
		{
			user = (Member)session.getAttribute(Member.class.toString( ));
		}
		if (user != null)
		{
			if (user.isPermited(Action.getInstance("LOG_IN")))
			{
				chain.doFilter(request, response);
			} else {
				HttpServletResponse httpResponse = (HttpServletResponse)response;
				httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, ResourceBundle.getBundle("ErrorMessages", response.getLocale( )).getString("BANNED"));	
			}
		} else {
			HttpServletResponse httpResponse = (HttpServletResponse)response;
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, ResourceBundle.getBundle("ErrorMessages", response.getLocale( )).getString("LOG_IN_REQUIRED"));
			/*httpResponse.sendRedirect(httpResponse.encodeRedirectURL("/"));*/
		}
	}

	@Override
	public void destroy ( ) { }
}