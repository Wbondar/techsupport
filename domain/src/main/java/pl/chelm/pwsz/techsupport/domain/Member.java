package pl.chelm.pwsz.techsupport.domain;

import java.util.Set;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class Member
extends Object
implements Identifiable<Member>
{
	private final String name;

	Member (long id, String name)
	{
		this(new Identificator<Member> (id), name);
	}

	Member (Identificator<Member> id, String name)
	{
		this.id   = id;
		this.name = name;
	}

	public String getName ( )
	{
		return this.name;
	}

	public Set<Issue> getIssues ( )
	{
		return Issue.getInstance(this);
	}

	public boolean isPermitted (Action givenAction)
	{
		return givenAction.isPermittedFor(this);
	}

	public Permission permitAction (Member granter, Action actionToBePermited)
	{
		return actionToBePermited.grantTo(granter, this);
	}

	public RevokedPermission forbidAction (Member forbidder, Action actionToBeForbidded)
	{
		return actionToBeForbidded.revokeFrom(forbidder, this);
	}

	private final Identificator<Member> id;

	public Identificator<Member> getId ( )
	{
		return this.id;
	}

	@Override
	public int hashCode ( )
	{
		return this.id.intValue( );
	}
}