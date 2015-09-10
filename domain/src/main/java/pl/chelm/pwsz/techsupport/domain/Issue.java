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
	public static Issue getInstance (Identificator<Issue> id)
	{
		IssueFactory factory = Factories.<IssueFactory>getInstance(IssueFactory.class);
		return factory.getInstance(id);
	}

	static Issue getInstance (Data data)
	{
		IssueFactory factory = Factories.<IssueFactory>getInstance(IssueFactory.class);
		return factory.getInstance(data);
	}

	public static Set<Issue> getInstances (Member issuer)
	{
		IssueFactory factory = Factories.<IssueFactory>getInstance(IssueFactory.class);
		return factory.getInstances(issuer);
	}

	public static Issue newInstance (Member issuer, String title, String message)
	{
		IssueFactory factory = Factories.<IssueFactory>getInstance(IssueFactory.class);
		return factory.newInstance(issuer, title, message);
	}

	private final Member issuer;
	private final String title;
	private final String message;

	Issue (long id, Member issuer, String title, String message)
	{
		this(new Identificator<Issue> (id), issuer, title, message);
	}

	Issue (Identificator<Issue> id, Member issuer, String title, String message)
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
		return Comment.getInstances(this);
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