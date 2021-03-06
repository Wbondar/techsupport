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
  <%@ include file="/jspf/nav.jsp" %>
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
    <%@ include file="/jspf/form_issue_create.jsp" %>
    <%@ include file="/jspf/form_log_in.jsp" %>
    <%@ include file="/jspf/form_member_create.jsp" %>
    <%@ include file="/jspf/form_session_destroy.jsp" %>
  </aside>
</body>
</html>
