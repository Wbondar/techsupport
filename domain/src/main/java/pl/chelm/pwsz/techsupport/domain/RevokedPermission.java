package pl.chelm.pwsz.techsupport.domain;

import java.util.Date;

final class RevokedPermission
extends Permission
{
	private final Permission granting;

	RevokedPermission (Member revoker, Permission granting, Date dateRevoked)
	{
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