<%@ page import="java.util.ResourceBundle" %>
<%@ page import="pl.chelm.pwsz.techsupport.domain.Member" %>
<%
  if (member != null)
  {
%>
<section>
	<h2><%= headers.getString("ISSUE_CREATE") %></h2>
	<form name='issue_create' action='/issue/create' method='POST' >
		<fieldset>
			<legend></legend>
			<label>
				<%= labels.getString("ISSUE_TITLE") %>
				<input type='text' name='title' />
			</label>
			<label>
				<%= labels.getString("ISSUE_DESCRIPTION") %>
				<textarea name='message' cols='30' rows='6'></textarea>
			</label>
		</fieldset>
		<input type='submit' />
	</form>
</section>
<%
  }
%>