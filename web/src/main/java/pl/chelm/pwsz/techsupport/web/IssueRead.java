package pl.chelm.pwsz.techsupport.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import pl.chelm.pwsz.techsupport.domain.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class IssueRead
extends HttpServlet
{
	@Override
	public void doGet (HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		Identificator<Issue> id = new Identificator<Issue> (request.getParameter("id"));
		Issue issue = Issue.getInstance(id);
		if (issue != null)
		{
			request.setAttribute(Issue.class.toString( ), issue);
			response.setStatus(HttpServletResponse.SC_OK);
			Page.ISSUE.include(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}