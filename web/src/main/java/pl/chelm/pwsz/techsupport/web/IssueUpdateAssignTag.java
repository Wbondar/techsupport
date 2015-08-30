package pl.chelm.pwsz.techsupport.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import pl.chelm.pwsz.techsupport.domain.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class IssueUpdateAssignTag
extends HttpServlet
{
	@Override
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		Member assigner = (Member)request.getSession(false).getAttribute(Member.class.toString( ));
		if (assigner == null)
		{
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Permission denied.");
		}
		Identificator<Issue> idOfIssue = new Identificator<Issue> (request.getParameter("issue_id"));
		Issue issue = Issue.getInstance(idOfIssue);
		if (issue == null)
		{
			throw new RuntimeException ("Issue is missing.");
		}
		String[] tagTitles = request.getParameterValues("tag_title");
		int i = 0;
		boolean success = true;
		for (i = 0; i < tagTitles.length; i++)
		{
			Tag tag = Tag.getInstance(tagTitles[i]);
			issue = issue.assignTag(assigner, tag);
			success = issue.containsTag(tag) && success;
		}
		if (success)
		{
			response.setStatus(HttpServletResponse.SC_OK);
			Page.ISSUE.setParameter("id", issue.getId( ));
			Page.ISSUE.redirect(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to proccess the request for unknown reason. Please check if input was correct.");
		}
	}
}