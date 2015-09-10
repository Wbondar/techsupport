package pl.chelm.pwsz.techsupport.domain;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class Action
extends Object
implements Identifiable<Action>
{
	public static Action newInstance (String label)
	{
		ActionFactory factory = Factories.<ActionFactory>getInstance(ActionFactory.class);
		return factory.newInstance(label);
	}

	public static Action getInstance (String label)
	{
		ActionFactory factory = Factories.<ActionFactory>getInstance(ActionFactory.class);
		return factory.getInstance(label);
	}

	public static Action getInstance (Identificator<Action> id)
	{
		ActionFactory factory = Factories.<ActionFactory>getInstance(ActionFactory.class);
		return factory.getInstance(id);
	}

	static Action getInstance (Data data)
	{
		ActionFactory factory = Factories.<ActionFactory>getInstance(ActionFactory.class);
		return factory.getInstance(data);
	}

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
			return false;
		}
		return permission.isValid( );
	}

	public Permission grantTo (Member grantor, Member grantee)
	{
		return Permission.newInstance(grantor, grantee, this);
	}

	public RevokedPermission revokeFrom (Member revoker, Member memberForActionToBeRevokedFrom)
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