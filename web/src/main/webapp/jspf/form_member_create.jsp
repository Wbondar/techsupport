<%@ page import="java.util.ResourceBundle" %>
<%@ page import="pl.chelm.pwsz.techsupport.domain.Member" %>
<%
  if (member != null)
  {
%>
<section>
  <h2><%= headers.getString("MEMBER_CREATE") %></h2>
  <form name='log_in' action='/member/create' method='POST' >
      <fieldset>
        <legend><%= labels.getString("MEMBER_CREDENTIALS") %></legend>
        <label>
          <%= labels.getString("USERNAME") %>
          <input type='text' name='username' />
        </label>
        <label>
          <%= labels.getString("PASSWORD") %>
          <input type='password' name='password' />
        </label>
      </fieldset>
      <input type='submit' />
  </form>
</section>
<%
  }
%>
