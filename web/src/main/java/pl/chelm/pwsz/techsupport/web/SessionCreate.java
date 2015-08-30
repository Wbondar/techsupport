package pl.chelm.pwsz.techsupport.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import pl.chelm.pwsz.techsupport.domain.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class SessionCreate
extends HttpServlet
{
	@Override
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		String username = request.getParameter("username");
		if (username == null || username.isEmpty( ))
		{
			throw new RuntimeException ("Username is missing.");
		}
		String password = request.getParameter("password");
		if (password == null || password.isEmpty( ))
		{
			throw new RuntimeException ("Password is missing.");
		}
		Member member = Member.getInstance(username, password);
		if (member != null)
		{
			HttpSession session = request.getSession(false);
			if (session != null)
			{
				session.invalidate( );
			}
			session = request.getSession( );
			session.setMaxInactiveInterval(60*10);
			session.setAttribute(Member.class.toString( ), member);
			response.setStatus(HttpServletResponse.SC_OK);
			Page.HOME.redirect(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Wrong username or password.");
		}
	}
}