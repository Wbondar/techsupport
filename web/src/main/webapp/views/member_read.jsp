<%@ page import="pl.chelm.pwsz.techsupport.domain.Member" %>
<!DOCTYPE html>
<html>
<head>
	<link rel='stylesheet' type='text/css' href='./css/layout.css' />
</head>
<body>
  <main>
    <article>
	    <%
	    	Member member = (Member)request.getAttribute(Member.class.toString( ));
	    %>
      <h1><%= member.getName( ) %></h1>
    </article>
  </main>
</body>
</html>