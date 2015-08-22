package pl.chelm.pwsz.techsupport.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import pl.chelm.pwsz.techsupport.domain.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class MemberCreate
extends HttpServlet
{
	@Override
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		String[] usernames = request.getParameterValues("username");
		String[] passwords = request.getParameterValues("password");
		int i = 0;
		for (i = 0; i < usernames.length; i++)
		{
			Member member = Member.getInstance(usernames[i], passwords[i]);
		}
	}
}