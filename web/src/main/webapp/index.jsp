<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="pl.chelm.pwsz.techsupport.domain.Member" %>
<%@ page import="pl.chelm.pwsz.techsupport.domain.Issue" %>
<%@ page import="pl.chelm.pwsz.techsupport.services.StringEscapeUtils" %>
<%
  Member member = (Member)session.getAttribute(Member.class.toString( ));
  ResourceBundle labels = ResourceBundle.getBundle("LabelsLocalization", response.getLocale( ));
  ResourceBundle headers = ResourceBundle.getBundle("HeadersLocalization", response.getLocale( ));
%>
<!DOCTYPE html>
<html>
<head>
  <link rel='stylesheet' type='text/css' href='<%= request.getServletContext( ).getContextPath() %>/css/layout.css' />
</head>
<body>
  <main>
    <h1>The State School of Higher Education in Chelm: Techsupport service.</h1>
    <section>
      <%
        if (member != null)
        {
          StringBuilder stringBuilder = new StringBuilder ( );
          stringBuilder.append("<h2>" + headers.getString("ISSUE_READ_BY_MEMBER") + " " +  member.getName( ) + ".</h2>");
          stringBuilder.append("<ul>");
          Set<Issue> issues = member.getIssues( );
          if (issues != null)
          {
            for (Issue issue : issues)
            {
              stringBuilder.append("<li>");
              stringBuilder.append(labels.getString("ISSUE_ID") + issue.getId( ) + ": ");
              stringBuilder.append("<a href='" + response.encodeURL("/issue?id=" + issue.getId( )) + "'>" + StringEscapeUtils.escapeHtml4(issue.getTitle( )) + "</a>");
              stringBuilder.append("</li>");
            }
            stringBuilder.append("</ul>");
            out.println(stringBuilder);
          }
        }
      %>
    </section>
  </main>
  <aside>
    <%@ include file="/jspf/form_issue_search.jsp" %>
    <%
      if (member != null)
      {
        StringBuilder stringBuilder = new StringBuilder ( );
        stringBuilder.append("<section>");
          stringBuilder.append("<h2>" + headers.getString("ISSUE_CREATE") + "</h2>");
          stringBuilder.append("<form name='issue_create' action='/issue/create' method='POST' >");
              stringBuilder.append("<input type='text' name='title' />");
              stringBuilder.append("<textarea name='message' cols='30' rows='6'></textarea>");
              stringBuilder.append("<input type='submit' />");
          stringBuilder.append("</form>");
        stringBuilder.append("</section>");
        stringBuilder.append("<section>");  
          stringBuilder.append("<h2>" + headers.getString("MEMBER_CREATE") + "</h2>");  
          stringBuilder.append("<form name='sign_up' action='/member/create' method='POST' >"); 
              stringBuilder.append("<input type='text' name='username' />");  
              stringBuilder.append("<input type='password' name='password' />");  
              stringBuilder.append("<input type='submit' />");  
          stringBuilder.append("</form>");  
        stringBuilder.append("</section>"); 
        stringBuilder.append("<section>");
          stringBuilder.append("<h2>" + headers.getString("EXIT") + "</h2>");
          stringBuilder.append("<form name='exit' action='/session/destroy' method='POST' >");
              stringBuilder.append("<input type='submit' />");
          stringBuilder.append("</form>");
        stringBuilder.append("</section>");
        out.println(stringBuilder);
      } else {
        StringBuilder stringBuilder = new StringBuilder ( );
        stringBuilder.append("<section>");  
          stringBuilder.append("<h2>" + headers.getString("LOG_IN") + "</h2>"); 
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
</body>
</html>
