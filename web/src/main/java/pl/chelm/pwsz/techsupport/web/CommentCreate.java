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
		String content = request.getParameter("content");
		Member author = (Member)request.getSession(false).getAttribute(Member.class.toString( ));
		Identificator<Comment> idOfParent = new Identificator<Comment> (request.getParameter("parent_id"));
		Comment parent = Comment.getInstance(idOfParent);
		if (parent != null)
		{
			Comment comment = Comment.newInstance(parent, author, content);
		} else {
			Identificator<Issue> idOfIssue = new Identificator<Issue> (request.getParameter("issue_id"));
			Issue issue = Issue.getInstance(idOfIssue);
			Comment comment = Comment.newInstance(issue, author, content);
		}
	}
}