package pl.chelm.pwsz.techsupport.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import pl.chelm.pwsz.techsupport.domain.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class IssueCreate
extends HttpServlet
{
	@Override
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		Member author = (Member)request.getSession(false).getAttribute(Member.class.toString( ));
		String[] titles   = request.getParameterValues("title");
		String[] messages = request.getParameterValues("message");
		int i = 0;
		boolean success = true;
		for (i = 0; i < titles.length; i++)
		{
			Issue issue = Issue.newInstance(author, titles[i], messages[i]);
			success = (issue != null) && success;
		}
		if (success)
		{
			response.setStatus(HttpServletResponse.SC_CREATED);
			Page.HOME.redirect(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to proccess the request for unknown reason. Please check if input was correct.");
		}
	}
}