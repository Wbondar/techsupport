<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="pl.chelm.pwsz.techsupport.domain.Comment" %>
<%@ page import="pl.chelm.pwsz.techsupport.domain.Issue" %>
<%@ page import="pl.chelm.pwsz.techsupport.domain.Tag" %>
<%@ page import="pl.chelm.pwsz.techsupport.services.StringEscapeUtils" %>
<%
  Issue issue = (Issue)request.getAttribute(Issue.class.toString( ));
  if (issue == null)
  {
    throw new RuntimeException ("Issue is missing.");
  }
  ResourceBundle labels = ResourceBundle.getBundle("LabelsLocalization", response.getLocale( ));
  ResourceBundle headers = ResourceBundle.getBundle("HeadersLocalization", response.getLocale( ));
  DateFormat commentDateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.DEFAULT, response.getLocale( ));
%>
<!DOCTYPE html>
<html>
<head>
  <link rel='stylesheet' type='text/css' href='<%= request.getServletContext( ).getContextPath() %>/css/layout.css' />
</head>
<body>
  <main>
    <article class='issue'>
      <header>
        <h1><%= labels.getString("ISSUE_TITLE")%> #<%= issue.getId( ) %>: <%= StringEscapeUtils.escapeHtml4(issue.getTitle( )) %></h1>
      </header>
      <footer>
        <p><%= StringEscapeUtils.escapeHtml4(issue.getIssuer( ).getName( )) %></p>
      </footer>
      <section class='description'>
        <h2><%= labels.getString("ISSUE_DESCRIPTION") %></h2>
        <p><%= StringEscapeUtils.escapeHtml4(issue.getMessage( )) %></p>
      </section>
      <section class='tags'>
        <h2><%= headers.getString("TAGS") %></h2>
        <%
          Set<Tag> tags = issue.getTags( );
          if (tags != null && !tags.isEmpty( ))
          {
            StringBuilder stringBuilder = new StringBuilder ( );
            stringBuilder.append("<ul>");
            for (Tag tag : tags)
            {
              stringBuilder.append("<li>");
              stringBuilder.append("<section class='tag'>");
              stringBuilder.append("<h3>" + StringEscapeUtils.escapeHtml4(tag.getTitle( )) + "</h3>");
              stringBuilder.append("<form name='" + StringEscapeUtils.escapeHtml4(tag.getTitle( )).toLowerCase( ).replaceAll("\\s+","") + "_unassign' method='POST' action='/issue/update/tag/unassign'>");
              stringBuilder.append("<input type='hidden' name='issue_id' value='" + issue.getId( ) + "' required />");
              stringBuilder.append("<input type='hidden' name='tag_id' value='" + tag.getId( ) + "' required />");
              stringBuilder.append("<input type='submit' />");
              stringBuilder.append("</form>");
              stringBuilder.append("</section>");
              stringBuilder.append("</li>");
            }
            stringBuilder.append("</ul>");
            out.println(stringBuilder.toString( ));
          }
        %>
      </section>
    </article>
    <section class='comments'>
      <h2><%= headers.getString("COMMENTS") %></h2>
      <%
        Set<Comment> comments = issue.getComments( );
        if (comments != null && !comments.isEmpty( ))
        {
          StringBuilder stringBuilder = new StringBuilder ( );
          for (Comment comment : comments)
          {
            stringBuilder.append("<article class='comment' id='" + comment.getId( ) + "'>");
              stringBuilder.append("<header>");
                stringBuilder.append("<h3>" + labels.getString("COMMENT_ID") + comment.getId( ) + ".</h3>");
              stringBuilder.append("</header>");
              stringBuilder.append(StringEscapeUtils.escapeHtml4(comment.getContent( )));
              stringBuilder.append("<footer>");
                stringBuilder.append("<p>" + commentDateFormat.format(comment.getDatePosted( )) + "</p>");
                stringBuilder.append("<p>" + StringEscapeUtils.escapeHtml4(comment.getAuthor( ).getName( )) + "</p>");
              stringBuilder.append("</footer>");
            stringBuilder.append("</article>");
          }
          out.println(stringBuilder.toString( ));
        }
      %>
    </section>
  </main>
  <aside>
    <section>
      <h2><%= headers.getString("ISSUE_UPDATE_TAG_ASSIGN") %></h2>
      <form name='tag_assign' method='POST' action='/issue/update/tag/assign'>
        <input type='hidden' name='issue_id' value='<%= issue.getId( ) %>' required />
        <input type='text' name='tag_title' required />
        <input type='submit' />
      </form>
    </section>
    <section>
      <h2><%= headers.getString("COMMENT_CREATE") %></h2>
      <form name='comment' method='POST' action='/comment/create'>
        <input type='hidden' name='issue_id' value='<%= issue.getId( ) %>' required />
        <textarea name='message' cols='30' rows='6' required></textarea>
        <input type='submit' />
      </form>
    </section>
  </aside>
</body>
</html>