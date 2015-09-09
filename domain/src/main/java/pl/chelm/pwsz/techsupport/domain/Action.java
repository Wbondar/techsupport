package pl.chelm.pwsz.techsupport.domain;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class Action
extends Object
implements Identifiable<Action>
{
	Action (long id, String label)
	{
		this(new Identificator<Action> (id), label);
	}

	Action (Identificator<Action> id, String label)
	{
		this.id    = id;
		this.label = label;
	}

	private final String label;
 
	public String getLabel ( )
	{
		return this.label;
	}

	public boolean isPermitedFor (Member member)
	{
		Permission permission = Permission.getInstance(member, this);
		if (permission == null)
		{
			throw new RuntimeException ("Failed to instantiate permission object.");
		}
		return permission.isValid( );
	}

	public Permission grantTo (Member grantor, Member grantee)
	{
		return Permission.newInstance(grantor, grantee, this);
	}

	public RevokedPermisson revokeFrom (Member revoker, Member memberForActionToBeRevokedFrom)
	{
		return RevokedPermission.newInstance(revoker, memberForActionToBeRevokedFrom, this);
	}

	private final Identificator<Action> id;

	public Identificator<Action> getId ( )
	{
		return this.id;
	}

	@Override
	public int hashCode ( )
	{
		return this.id.intValue( );
	}
}