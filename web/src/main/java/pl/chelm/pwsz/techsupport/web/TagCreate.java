package pl.chelm.pwsz.techsupport.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import pl.chelm.pwsz.techsupport.domain.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class TagCreate
extends HttpServlet
{
	@Override
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		String[] titles = request.getParameterValues("title");
		int i = 0;
		for (i = 0; i < titles.length; i++)
		{
			Tag tag = Tag.getInstance(titles[i]);
		}
	}
}