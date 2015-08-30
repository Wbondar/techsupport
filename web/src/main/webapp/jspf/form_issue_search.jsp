<section>
	<h2><%= headers.getString("ISSUE_SEARCH") %></h2>
	<form name='issue_search_by_title' action='/issue' method='GET' >
		<fieldset>
			<legend><%= labels.getString("SEARCH_BY_TITLE") %></legend>
			<label>
				<%= labels.getString("ISSUE_TITLE") %>
				<input type='text' name='issue_title' />
			</label>
		</fieldset>
		<input type='submit' />
	</form>
	<form name='issue_search_by_date' action='/issue' method='GET' >
		<fieldset>
			<legend><%= labels.getString("SEARCH_BY_DATE") %></legend>
			<label>
				<%= labels.getString("ISSUE_DATE_ISSUED_AFTER") %>
				<input type='date' name='after' />
			</label>
			<label>
				<%= labels.getString("ISSUE_DATE_ISSUED_BEFORE") %>
				<input type='date' name='before' />
			</label>
		</fieldset>
		<input type='submit' />
	</form>
	<form name='issue_search_by_tag' action='/issue' method='GET' >
		<fieldset>
			<legend><%= labels.getString("SEARCH_BY_TAG") %></legend>
			<label>
				<%= labels.getString("TAG_TITLE") %>
				<input type='text' name='tag_title' />
			</label>
		</fieldset>
		<input type='submit' />
	</form>
	<form name='issue_search_by_issuer' action='/issue' method='GET'>
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