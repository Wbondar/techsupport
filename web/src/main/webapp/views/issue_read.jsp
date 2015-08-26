<!DOCTYPE html>
<html>
<head>
</head>
<body>
  <main>
    <article>
      <h1>Issue #<%= issue.getId( ) %>: <%= issue.getTitle( ) %></h1>
      <section>
        <h2>Description.</h2>
        <%= issue.getMessage( ) %>
      </section>
      <section>
        <h2>Tags.</h2>

      </section>
    </article>
  </main>
</body>
</html>