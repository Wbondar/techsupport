package pl.chelm.pwsz.techsupport.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import pl.chelm.pwsz.techsupport.domain.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class CommentCreate
extends HttpServlet
{
	@Override
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		String message = request.getParameter("message");
		Member author = (Member)request.getSession(false).getAttribute(Member.class.toString( ));
		if (author == null)
		{
			throw new RuntimeException ("Author is missing.");
		}
		Identificator<Issue> idOfIssue = new Identificator<Issue> (request.getParameter("issue_id"));
		Issue issue = Issue.getInstance(idOfIssue);
		if (issue == null)
		{
			throw new RuntimeException ("Issue is missing.");
		}
		Comment comment = issue.comment(author, message);
		if (comment != null)
		{
			response.setStatus(HttpServletResponse.SC_CREATED);
			Page.ISSUE.setParameter("id", issue.getId( ));
			Page.ISSUE.redirect(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to proccess the request for unknown reason. Please check if input was correct.");
		}
	}
}