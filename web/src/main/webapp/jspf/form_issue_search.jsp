<section>
	<h2><%= headers.getString("ISSUE_SEARCH") %></h2>
	<form name='issue_search' action='/issues' method='GET' >
		<fieldset>
			<legend><%= labels.getString("SEARCH_BY_TITLE") %></legend>
			<label>
				<%= labels.getString("ISSUE_TITLE") %>
				<input type='text' name='mentionings' />
			</label>
		</fieldset>
		<fieldset>
			<legend><%= labels.getString("SEARCH_BY_DATE") %></legend>
			<label>
				<%= labels.getString("ISSUE_DATE_ISSUED_AFTER") %>
				<input type='date' name='after' placeholder='rrrr-mm-dd' />
			</label>
			<label>
				<%= labels.getString("ISSUE_DATE_ISSUED_BEFORE") %>
				<input type='date' name='before' placeholder='rrrr-mm-dd' />
			</label>
		</fieldset>
		<fieldset>
			<legend><%= labels.getString("SEARCH_BY_TAG") %></legend>
			<label>
				<%= labels.getString("TAG_TITLE") %>
				<input type='text' name='tag_title' />
			</label>
		</fieldset>
		<fieldset>
			<legend><%= labels.getString("SEARCH_BY_USERNAME") %></legend>
			<label>
				<%= labels.getString("ISSUER_NAME") %>
				<input type='text' name='issuer_name' />
			</label>
		</fieldset>
		<input type='submit' />
	</form>
</section>