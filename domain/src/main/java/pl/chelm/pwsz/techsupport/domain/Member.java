package pl.chelm.pwsz.techsupport.domain;

import java.util.Set;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class Member
extends Object
implements Identifiable<Member>
{
	public static Member getInstance (String username, String password)
	{
		MemberFactory factory = Factories.<MemberFactory>getInstance(MemberFactory.class);
		return factory.getInstance(username, password);
	}

	public static Member getInstance (String username)
	{
		MemberFactory factory = Factories.<MemberFactory>getInstance(MemberFactory.class);
		return factory.getInstance(username);
	}

	public static Member getInstance (Identificator<Member> id)
	{
		MemberFactory factory = Factories.<MemberFactory>getInstance(MemberFactory.class);
		return factory.getInstance(id);
	}

	public static Member newInstance (String username, String password)
	{
		MemberFactory factory = Factories.<MemberFactory>getInstance(MemberFactory.class);
		return factory.newInstance(username, password);
	}

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
		return Issue.getInstances(this);
	}

	public boolean isPermited (Action givenAction)
	{
		return givenAction.isPermitedFor(this);
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