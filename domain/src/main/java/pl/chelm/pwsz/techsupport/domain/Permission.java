package pl.chelm.pwsz.techsupport.domain;

import java.util.Date;

import pl.chelm.pwsz.techsupport.services.Identifiable;
import pl.chelm.pwsz.techsupport.services.Identificator;

class Permission
extends Object
implements Identifiable<Permission>
{
	public static Permission newInstance (Member grantor, Member grantee, Action permitedAction)
	{
		PermissionFactory factory = Factories.<PermissionFactory>getInstance(PermissionFactory.class);
		return factory.newInstance(grantor, grantee, permitedAction);
	}

	public static Permission getInstance (Member supposedGrantee, Action supposedPermitedAction)
	{
		PermissionFactory factory = Factories.<PermissionFactory>getInstance(PermissionFactory.class);
		return factory.getInstance(supposedGrantee, supposedPermitedAction);
	}

	Permission (Identificator<Permission> id, Member grantor, Member grantee, Action permitedAction, Date dateGranted, boolean valid)
	{
		this.id              = id;
		this.grantor         = grantor;
		this.grantee         = grantee;
		this.permitedAction  = permitedAction;
		this.since           = dateGranted;
		this.valid           = valid;
	}

	private final Member grantor;

	public Member getGrantor ( )
	{
		return this.grantor;
	}

	private final Member grantee;

	public Member getGrantee ( )
	{
		return this.grantee;
	}

	private final Date   since;

	public Date getDateGranted ( )
	{
		return this.since;
	}

	private final Action permitedAction;

	public Action getAction ( )
	{
		return this.permitedAction;
	}

	private final boolean valid;

	public boolean isValid ( )
	{
		return this.valid;
	}

	private final Identificator<Permission> id;

	@Override
	public Identificator<Permission> getId ( )
	{
		return this.id;
	}
}