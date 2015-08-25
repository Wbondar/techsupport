package pl.chelm.pwsz.techsupport.web;

import java.io.IOException;
import java.io.PrintWriter;

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
		if (usernames.length < passwords.length || usernames.length <= 0)
		{
			throw new RuntimeException ("Username is missing.");
		}
		if (usernames.length > passwords.length)
		{
			throw new RuntimeException ("Password is missing.");
		}
		int i = 0;
		PrintWriter writer = response.getWriter( );
		for (i = 0; i < usernames.length; i++)
		{
			Member member = Member.getInstance(usernames[i], passwords[i]);
			writer.println(member);
		}
		writer.close( );
	}
}