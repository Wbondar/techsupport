<%@ page import="pl.chelm.pwsz.techsupport.domain.Comment" %>
<%@ page import="pl.chelm.pwsz.techsupport.domain.Issue" %>
<%@ page import="pl.chelm.pwsz.techsupport.domain.Tag" %>
<%@ page import="java.util.Set" %>
<%
  Issue issue = (Issue)request.getAttribute(Issue.class.toString( ));
  if (issue == null)
  {
    throw new RuntimeException ("Issue is missing.");
  }
%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
  <aside>
    <section>
      <h2>Add tag.</h2>
      <form name='tag_assign' method='POST' action='/issue/update/tag/assign'>
        <input type='hidden' name='issue_id' value='<%= issue.getId( ) %>' required />
        <input type='text' name='tag_title' required />
        <input type='submit' />
      </form>
    </section>
    <section>
      <h2>Comment.</h2>
      <form name='comment' method='POST' action='/comment/create'>
        <input type='hidden' name='issue_id' value='<%= issue.getId( ) %>' required />
        <textarea name='message' cols='30' rows='6' required></textarea>
        <input type='submit' />
      </form>
    </section>
  </aside>
  <main>
    <article>
      <h1>Issue #<%= issue.getId( ) %>: <%= issue.getTitle( ) %></h1>
      <section>
        <h2>Description.</h2>
        <%= issue.getMessage( ) %>
      </section>
      <section>
        <h2>Tags.</h2>
        <%
          Set<Tag> tags = issue.getTags( );
          if (tags != null && !tags.isEmpty( ))
          {
            StringBuilder stringBuilder = new StringBuilder ( );
            stringBuilder.append("<ul>");
            for (Tag tag : tags)
            {
              stringBuilder.append("<li>");
              stringBuilder.append(tag.getTitle( ));
              stringBuilder.append("<form name='" + tag.getTitle( ).toLowerCase( ).replaceAll("\\s+","") + "_unassign' method='POST' action='/issue/update/tag/unassign'>");
              stringBuilder.append("<input type='hidden' name='issue_id' value='" + issue.getId( ) + "' required />");
              stringBuilder.append("<input type='hidden' name='tag_id' value='" + tag.getId( ) + "' required />");
              stringBuilder.append("<input type='submit' />");
              stringBuilder.append("</form>");
              stringBuilder.append("</li>");
            }
            stringBuilder.append("</ul>");
            out.println(stringBuilder.toString( ));
          }
        %>
      </section>
      <section>
        <h2>Comments.</h2>
        <%
          Set<Comment> comments = issue.getComments( );
          if (comments != null && !comments.isEmpty( ))
          {
            StringBuilder stringBuilder = new StringBuilder ( );
            for (Comment comment : comments)
            {
              stringBuilder.append("<article>");
                stringBuilder.append("<h3>Comment #" + comment.getId( ) + ".</h3>");
                stringBuilder.append(comment.getContent( ));
              stringBuilder.append("</article>");
            }
            out.println(stringBuilder.toString( ));
          }
        %>
      </section>
    </article>
  </main>
</body>
</html>