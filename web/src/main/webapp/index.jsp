<%@ page import="java.util.Set" %>
<%@ page import="pl.chelm.pwsz.techsupport.domain.Member" %>
<%@ page import="pl.chelm.pwsz.techsupport.domain.Issue" %>
<%
	Member member = (Member)session.getAttribute(Member.class.toString( ));
%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<aside>
		<%
			if (member != null)
			{
				StringBuilder stringBuilder = new StringBuilder ( );
				stringBuilder.append("<section>");
					stringBuilder.append("<h2>Exit.</h2>");
					stringBuilder.append("<form name='exit' action='/session/destroy' method='POST' >");
					    stringBuilder.append("<input type='submit' />");
					stringBuilder.append("</form>");
				stringBuilder.append("</section>");
				stringBuilder.append("<section>");
					stringBuilder.append("<h2>New issue.</h2>");
					stringBuilder.append("<form name='issue_create' action='/issue/create' method='POST' >");
					    stringBuilder.append("<input type='text' name='title' />");
					    stringBuilder.append("<textarea name='message' cols='30' rows='6'></textarea>");
					    stringBuilder.append("<input type='submit' />");
					stringBuilder.append("</form>");
				stringBuilder.append("</section>");
				out.println(stringBuilder);
			} else {
				StringBuilder stringBuilder = new StringBuilder ( );
				stringBuilder.append("<section>");	
					stringBuilder.append("<h2>Sign up.</h2>");	
					stringBuilder.append("<form name='sign_up' action='/member/create' method='POST' >");	
					    stringBuilder.append("<input type='text' name='username' />");	
					    stringBuilder.append("<input type='password' name='password' />");	
					    stringBuilder.append("<input type='submit' />");	
					stringBuilder.append("</form>");	
				stringBuilder.append("</section>");	
				stringBuilder.append("<section>");	
					stringBuilder.append("<h2>Log in.</h2>");	
					stringBuilder.append("<form name='log_in' action='/session/create' method='POST' >");	
					    stringBuilder.append("<input type='text' name='username' />");	
					    stringBuilder.append("<input type='password' name='password' />");	
					    stringBuilder.append("<input type='submit' />");	
					stringBuilder.append("</form>");	
				stringBuilder.append("</section>");	
				out.println(stringBuilder);
			}	
		%>
	</aside>
	<main>
		<h1>The State School of Higher Education in Chelm: Techsupport service.</h1>
		<%
			if (member != null)
			{
				StringBuilder stringBuilder = new StringBuilder ( );
				stringBuilder.append("<ul>");
				Set<Issue> issues = member.getIssues( );
				if (issues != null)
				{
					for (Issue issue : issues)
					{
						stringBuilder.append("<li>");
						stringBuilder.append("<a href='" + response.encodeURL("/issue?id=" + issue.getId( )) + "'>" + issue.getTitle( ) + "</a>");
						stringBuilder.append("</li>");
					}
					stringBuilder.append("</ul>");
					out.println(stringBuilder);
				}
			}
		%>
	</main>
</body>
</html>
