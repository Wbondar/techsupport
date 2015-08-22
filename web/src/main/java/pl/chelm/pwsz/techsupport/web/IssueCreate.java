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
		String[] titles   = request.getParameterValues("title");
		String[] messages = request.getParameterValues("message");
		Member author = (Member)request.getSession(false).getAttribute(Member.class.toString( ));
		int i = 0;
		for (i = 0; i < titles.length; i++)
		{
			Issue issue = Issue.newInstance(author, titles[i], messages[i]);
		}
	}
}