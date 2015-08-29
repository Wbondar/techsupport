package pl.chelm.pwsz.techsupport.web;

import java.io.IOException;

import javax.servlet.ServletException;

import javax.servlet.http.*;

import java.util.Locale;

enum Page
{
	  HOME ("/", "/index.jsp")
	, ISSUE ("/issue", "/views/issue_read.jsp");

	private static final Locale locale = new Locale ("pl_PL");

	private final String path;
	private final String jsp;

	private Page (String path, String jsp)
	{
		this.path = path;
		this.jsp = jsp;
	}

	public void redirect (HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		response.setContentType("text/html");
		response.sendRedirect(response.encodeURL(this.rewriteURL(this.path)));
	}

	private final java.util.Map<String, String> parameters = new java.util.HashMap<String, String> ( );

	public void setParameter (String key, Object value)
	{
		this.parameters.put(key, value.toString( ));
	}

	public void setParameter (String key, String value)
	{
		this.parameters.put(key, value);
	}

	private String rewriteURL (String url)
	{
		StringBuilder targetURL = new StringBuilder (url);
		if (!this.parameters.isEmpty( ))
		{
			targetURL.append("?");
			for (String key : this.parameters.keySet( ))
			{
				targetURL.append(key + "=" + this.parameters.get(key) + "&");
			}
			targetURL.delete(targetURL.length( ) - 1, targetURL.length( ));
			this.parameters.clear( );
		}
		return targetURL.toString( );
	}

	public void include (HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		response.setContentType("text/html");
		request.getRequestDispatcher(response.encodeURL(this.rewriteURL(this.jsp))).include(request, response);
	}
}