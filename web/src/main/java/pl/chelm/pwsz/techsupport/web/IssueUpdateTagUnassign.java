package pl.chelm.pwsz.techsupport.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import pl.chelm.pwsz.techsupport.domain.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class IssueUpdateTagUnassign
extends HttpServlet
{
	@Override
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		Member unassigner = (Member)request.getSession(false).getAttribute(Member.class.toString( ));
		Identificator<Issue> idOfIssue = new Identificator<Issue> (request.getParameter("issue_id"));
		Issue issue = Issue.getInstance(idOfIssue);
		String[] idOfTags = request.getParameterValues("tag_id");
		int i = 0;
		boolean success = true;
		for (i = 0; i < idOfTags.length; i++)
		{
			Identificator<Tag> idOfTag = new Identificator<Tag> (idOfTags[i]);
			Tag tag = Tag.getInstance(idOfTag);
			if (tag != null)
			{
				issue = issue.unassignTag(unassigner, tag);
				success = (!issue.containsTag(tag)) && success;
			} else {
				success = true && success;
			}
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