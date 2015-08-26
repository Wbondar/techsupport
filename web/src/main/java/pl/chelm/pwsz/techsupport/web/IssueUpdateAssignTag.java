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
		Identificator<Issue> idOfIssue = new Identificator<Issue> (request.getParameter("issue_id"));
		Issue issue = Issue.getInstance(idOfIssue);
		if (issue == null)
		{
			throw new RuntimeException ("Issue is missing.");
		}
		Member assigner = (Member)request.getSession(false).getAttribute(Member.class.toString( ));
		if (assigner == null)
		{
			throw new RuntimeException ("Assigner is missing.");
		}
		String[] tagTitles = request.getParameterValues("tag_title");
		int i = 0;
		for (i = 0; i < tagTitles.length; i++)
		{
			Tag tag = Tag.getInstance(tagTitles[i]);
			issue = issue.assignTag(assigner, tag);
		}
	}
}