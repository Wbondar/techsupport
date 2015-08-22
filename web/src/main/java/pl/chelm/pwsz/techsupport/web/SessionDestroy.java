package pl.chelm.pwsz.techsupport.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import pl.chelm.pwsz.techsupport.domain.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class SessionDestroy
extends HttpServlet
{
	@Override
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		HttpSession session = request.getSession(false);
		if (session != null)
		{
			session.invalidate( );
		}
	}
}