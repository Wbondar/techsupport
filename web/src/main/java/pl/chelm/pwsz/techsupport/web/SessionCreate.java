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
		String password = request.getParameter("password");
		Member member = Member.getInstance(username, password);
		HttpSession session = request.getSession(false);
		if (session != null)
		{
			session.invalidate( );
		}
		session = request.getSession( );
		session.setMaxInactiveInterval(60*10);
		session.setAttribute(Member.class.toString( ), member);
	}
}