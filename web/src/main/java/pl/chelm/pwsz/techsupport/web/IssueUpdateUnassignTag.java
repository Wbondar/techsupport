package pl.chelm.pwsz.techsupport.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import pl.chelm.pwsz.techsupport.domain.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class IssueUpdateUnassignTag
extends HttpServlet
{
	@Override
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		Identificator<Issue> idOfIssue = new Identificator<Issue> (request.getParameter("issue_id"));
		Issue issue = Issue.getInstance(idOfIssue);
		Member unassigner = (Member)request.getSession(false).getAttribute(Member.class.toString( ));
		String[] idOfTags = request.getParameterValues("tag_id");
		int i = 0;
		for (i = 0; i < idOfTags.length; i++)
		{
			Identificator<Tag> idOfTag = new Identificator<Tag> (idOfTags[i]);
			Tag tag = Tag.getInstance(idOfTag);
			issue = issue.unassignTag(unassigner, tag);
		}
	}
}