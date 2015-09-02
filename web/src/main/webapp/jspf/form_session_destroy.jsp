<%@ page import="java.util.ResourceBundle" %>
<%@ page import="pl.chelm.pwsz.techsupport.domain.Member" %>
<%
  if (member != null)
  {
%>
<section>
	<h2><%= headers.getString("EXIT") %></h2>
	<form name='exit' action='/session/destroy' method='POST' >
		<input type='submit' />
	</form>
</section>
<%
  }
%>