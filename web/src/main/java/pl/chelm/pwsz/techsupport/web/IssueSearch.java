package pl.chelm.pwsz.techsupport.web;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Collection;
import java.util.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import pl.chelm.pwsz.techsupport.domain.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class IssueSearch
extends HttpServlet
{
	@Override
	public void doGet (HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		IssueSearcher searcher = IssueSearcher.getInstance( );
		int i = 0;
		String[] mentionings = request.getParameterValues("mentionings");
		for (i = 0; i < mentionings.length; i++)
		{
			searcher.mentions(mentionings[i]);
		}
		DateFormat dateFormat = (DateFormat)	new SimpleDateFormat("yyyy-MM-dd");
		String after = request.getParameter("after");
		if (after != null && !after.isEmpty( ))
		{
			Date date = null;
			try
			{
				date = dateFormat.parse(after);
			} catch (Exception e) {
				throw new RuntimeException ("Failed to parse date.", e);
			}
			if (date != null)
			{
				searcher.issuedAfter(date);
			}
		}
		String before = request.getParameter("before");
		if (before != null && !before.isEmpty( ))
		{
			Date date = null;
			try
			{
				date = dateFormat.parse(before);
			} catch (Exception e) {
				throw new RuntimeException ("Failed to parse date.", e);
			}
			if (date != null)
			{
				searcher.issuedBefore(date);
			}
		}
		String[] tagTitles = request.getParameterValues("tag_title");
		for (i = 0; i < tagTitles.length; i++)
		{
			String tagTitle = tagTitles[i];
			if (tagTitle != null && !tagTitle.isEmpty( ))
			{
				Tag tag = Tag.getInstance(tagTitle);
				if (tag == null)
				{
					throw new RuntimeException ("There is no tag of title \"" + tagTitle + "\".");
				}
				searcher.containsTag(tag);
			}
		}
		String issuerName = request.getParameter("issuer_name");
		if (issuerName != null && !issuerName.isEmpty( ))
		{
			Member issuer = Member.getInstance(issuerName);
			if (issuer == null)
			{
				throw new RuntimeException ("There is no member of name " + issuerName + ".");
			}
			searcher.issuedBy(issuer);
		}
		Collection<Issue> issues = searcher.search( );
		if (issues != null && !issues.isEmpty( ))
		{
			response.setStatus(HttpServletResponse.SC_OK);
			request.setAttribute("issues", issues);
			Page.ISSUES.include(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, java.util.ResourceBundle.getBundle("ErrorMessages", response.getLocale( )).getString("ISSUE_NOT_FOUND"));
		}
	}
}