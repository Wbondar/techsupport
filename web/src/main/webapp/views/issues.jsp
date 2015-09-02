<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="pl.chelm.pwsz.techsupport.domain.Issue" %>
<%@ page import="pl.chelm.pwsz.techsupport.services.StringEscapeUtils" %>
<!DOCTYPE html>
<html>
<head>
  <link rel='stylesheet' type='text/css' href='<%= request.getServletContext( ).getContextPath( ) %>/css/layout.css' />
</head>
<body>
	<main>
		<h1><%= ResourceBundle.getBundle("HeadersLocalization", response.getLocale( )).getString("ISSUES_FOUND") %></h1>
		<%
			Collection<Issue> issues = (Collection<Issue>)request.getAttribute("issues");
			if (issues != null && !issues.isEmpty( ))
			{
				StringBuilder stringBuilder = new StringBuilder ( );
				stringBuilder.append("<ul>");
				for (Issue issue : issues)
				{
					stringBuilder.append("<li><a href='" + response.encodeURL("/issue?id=" + issue.getId( )) + "'>" + StringEscapeUtils.escapeHtml4(issue.getTitle( )) + "</a></li>");
				}
				stringBuilder.append("</ul>");
				out.println(stringBuilder);
			}
		%>
	</main>
</body>
</html>
