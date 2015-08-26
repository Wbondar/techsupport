package pl.chelm.pwsz.techsupport.domain;

/* TODO: Implement immutable class for handling tags assignation. */

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class Issue
extends Object
implements Identifiable<Issue>
{
	private static void cache (Issue issue) 
	{
		Cache<Issue> cache = CacheFactory.<Issue>getInstance(Issue.class);
		cache.put(issue);
	}

	private static Issue readFromCache (Identificator<Issue> id)
	{
		Cache<Issue> cache = CacheFactory.<Issue>getInstance(Issue.class);
		return cache.get(id);
	}

	private static Data readFromDatabase (Identificator<Issue> id)
	{
		IssueDatasource datasource = DatasourceFactory.<IssueDatasource>getInstance(IssueDatasource.class);
		return datasource.read(id.intValue( ));
	}

	private static int writeToDatabase (Member issuer, String title, String message)
	{
		IssueDatasource datasource = DatasourceFactory.<IssueDatasource>getInstance(IssueDatasource.class);
		return datasource.create(issuer.getId( ).intValue( ), title, message);
	}

	public static Issue newInstance (Member issuer, String title, String message)
	{
		int id = Issue.writeToDatabase(issuer, title, message);
		Issue issue = new Issue (id, issuer, title, message);
		Issue.cache(issue);
		return issue;
	}

	public static Issue getInstance (Identificator<Issue> id)
	{
		Issue issue = Issue.readFromCache(id);
		if (issue == null)
		{
			Data data = readFromDatabase(id);
			if (data == null)
			{
				return null;
			}
			Identificator<Member> idOfMember = new Identificator<Member> (data.<Integer>get(Integer.class, "issuer_id"));
			Member issuer = Member.getInstance(idOfMember);
			issue = new Issue(id, issuer, data.<String>get(String.class, "title"), data.<String>get(String.class, "message"));
			Issue.cache(issue);
		}
		return issue;
	}

	private final Member issuer;
	private final String title;
	private final String message;

	private Issue (int id, Member issuer, String title, String message)
	{
		this(new Identificator<Issue> (id), issuer, title, message);
	}

	private Issue (Identificator<Issue> id, Member issuer, String title, String message)
	{
		this.id      = id;
		this.issuer  = issuer;
		this.title   = title;
		this.message = message;
	}

	public String getTitle ( )
	{
		return this.title;
	}

	public String getMessage ( )
	{
		return this.message;
	}

	public Member getIssuer ( )
	{
		return this.issuer;
	}

	public Issue assignTag (Member whoIsAssigning, Tag tagToAssign)
	{
		TagAssignation.newInstance(whoIsAssigning, this, tagToAssign);
		return this;
	}

	public Issue unassignTag (Member whoIsUnassigning, Tag tagToUnassign)
	{
		TagAssignation.destroyInstance(whoIsUnassigning, this, tagToUnassign);
		return this;
	}

	public Set<Tag> getTags ( )
	{
		return TagAssignation.getTagsAssignedTo(this);
	}

	public boolean containsTag (Tag tagToCheck)
	{
		return this.getTags( ).contains(tagToCheck);
	}

	public Comment comment (Member commentator, String message)
	{
		return Comment.newInstance(this, commentator, message);
	}

	/* TODO: Implement proper retrieving of comments. */

	public Set<Comment> getComments ( )
	{
		return null;
	}

	private final Identificator<Issue> id;

	public Identificator<Issue> getId ( )
	{
		return this.id;
	}

	@Override
	public int hashCode ( )
	{
		return this.id.intValue( );
	}
}