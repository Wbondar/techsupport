package pl.chelm.pwsz.techsupport.domain;

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
		return datasource.read(id.longValue( ));
	}

	private static long writeToDatabase (Member issuer, String title, String message)
	{
		IssueDatasource datasource = DatasourceFactory.<IssueDatasource>getInstance(IssueDatasource.class);
		return datasource.create(issuer.getId( ).longValue( ), title, message);
	}

	public static Issue newInstance (Member issuer, String title, String message)
	{
		long id = Issue.writeToDatabase(issuer, title, message);
		Issue issue = new Issue (id, issuer, title, message);
		Issue.cache(issue);
		return issue;
	}

	public static Issue getInstance (Identificator<Issue> id)
	{
		Issue issue = Issue.readFromCache(id);
		if (issue == null)
		{
			Data data = Issue.readFromDatabase(id);
			if (data == null)
			{
				return null;
			}
			issue = Issue.getInstance(data);
		}
		return issue;
	}

	private static Collection<Data> readFromDatabaseByIssuer (Identificator<Member> idOfIssuer)
	{
		IssueDatasource datasource = DatasourceFactory.<IssueDatasource>getInstance(IssueDatasource.class);
		return datasource.readByIssuer (idOfIssuer.longValue( ));
	}

	static Issue getInstance (Data data)
	{
		Long idOfIssueValue = data.<Long>get(Long.class, "id");
		if (idOfIssueValue == null)
		{
			throw new RuntimeException ("Data is malformed.");
		}
		Identificator<Issue> idOfIssue = new Identificator<Issue> (idOfIssueValue);
		Issue issue = Issue.readFromCache(idOfIssue);
		if (issue == null)
		{
			Identificator<Member> idOfIssuer = new Identificator<Member> (data.<Long>get(Long.class, "issuer_id"));;
			Member issuer = Member.getInstance(idOfIssuer);
			String title = data.<String>get(String.class, "title");
			String message = data.<String>get(String.class, "message");
			issue = new Issue (idOfIssue, issuer, title, message);
			Issue.cache(issue);
		}
		return issue;
	}

	public static Set<Issue> getInstance (Member issuer)
	{
		Collection<Data> collectionOfData = Issue.readFromDatabaseByIssuer (issuer.getId( ));
		if (collectionOfData == null)
		{
			return null;
		}
		Set<Issue> setOfIssues = new HashSet<Issue> ( );
		for (Data data : collectionOfData)
		{
			Issue issue = Issue.getInstance(data);
			if (issue != null)
			{
				setOfIssues.add(issue);
			}
		}
		return Collections.<Issue>unmodifiableSet(setOfIssues);
	}

	private final Member issuer;
	private final String title;
	private final String message;

	private Issue (long id, Member issuer, String title, String message)
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
		return Comment.getInstance(this);
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