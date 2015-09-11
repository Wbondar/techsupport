package pl.chelm.pwsz.techsupport.web;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import pl.chelm.pwsz.techsupport.domain.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class MemberCreate
extends HttpServlet
{
	@Override
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		Member creator = (Member)request.getSession(false).getAttribute(Member.class.toString( ));
		if (creator.isPermited(Action.getInstance("CREATE_MEMBER")))
		{
			String[] usernames = request.getParameterValues("username");
			String[] passwords = request.getParameterValues("password");
			if (usernames.length < passwords.length || usernames.length <= 0)
			{
				throw new RuntimeException ("Username is missing.");
			}
			if (usernames.length > passwords.length)
			{
				throw new RuntimeException ("Password is missing.");
			}
			int i = 0;
			boolean success = true;
			for (i = 0; i < usernames.length; i++)
			{
				Member member = Member.newInstance(usernames[i], passwords[i]);
				success = (member != null) && success;
			}
			if (success)
			{
				response.setStatus(HttpServletResponse.SC_CREATED);
				Page.HOME.redirect(request, response);
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to proccess the request for unknown reason. Please check if input was correct.");
			}
		} else {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, ResourceBundle.getBundle("ErrorMessages", response.getLocale( )).getString("FORBIDDEN"));	
		}
	}
}