package pl.chelm.pwsz.techsupport.web;

import java.io.IOException;

import javax.servlet.ServletException;

import javax.servlet.http.*;

enum Page
{
	HOME ("/");

	private final String path;

	private Page (String path)
	{
		this.path = path;
	}

	public void redirect (HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		response.setContentType("text/html");
		response.sendRedirect(this.path);
	}
}