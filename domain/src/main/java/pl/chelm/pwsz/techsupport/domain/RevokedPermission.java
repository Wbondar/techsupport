package pl.chelm.pwsz.techsupport.domain;

import java.util.Date;

import pl.chelm.pwsz.techsupport.services.Identifiable;
import pl.chelm.pwsz.techsupport.services.Identificator;

final class RevokedPermission
extends Permission
{
	public static RevokedPermission newInstance (Member revoker, Permission permissionToRevoke)
	{
		PermissionFactory factory = Factories.<PermissionFactory>getInstance(PermissionFactory.class);
		return factory.newRevokedInstance(revoker, permissionToRevoke);
	}

	public static RevokedPermission newInstance (Member revoker, Member revokee, Action forbidenAction)
	{
		PermissionFactory factory = Factories.<PermissionFactory>getInstance(PermissionFactory.class);
		Permission permissionToRevoke = factory.getInstance(revokee, forbidenAction);
		if (permissionToRevoke == null)
		{
			return null;
		}
		return RevokedPermission.newInstance(revoker, permissionToRevoke);
	}

	private final Permission granting;

	RevokedPermission (Member revoker, Permission granting, Date dateRevoked)
	{
		super(granting.getId( ), granting.getGrantor( ), granting.getGrantee( ), granting.getAction( ), granting.getDateGranted( ), false);
		this.revoker  = revoker;
		this.granting = granting;
		this.until    = dateRevoked;
	}

	@Override
	public Member getGrantor ( )
	{
		return this.granting.getGrantor( );
	}

	@Override
	public Member getGrantee ( )
	{
		return this.granting.getGrantee( );
	}

	@Override
	public Date getDateGranted ( )
	{
		return this.granting.getDateGranted( );
	}

	@Override
	public Action getAction ( )
	{
		return this.granting.getAction( );
	}

	private final Member             revoker;

	public Member getRevoker ( )
	{
		return this.revoker;
	}

	private final Date               until;

	public Date getDateRevoked ( )
	{
		return this.until;
	}

	@Override
	public boolean isValid ( )
	{
		return false;
	}

	@Override
	public Identificator<Permission> getId( )
	{
		return this.granting.getId( );
	}
}