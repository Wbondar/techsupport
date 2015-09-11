package pl.chelm.pwsz.techsupport.domain;

import java.util.Date;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

final class PermissionFactory
extends CacheFactory<Permission, PermissionDatasource>
{
	PermissionFactory (Class<Permission> entitiesType, Class<PermissionDatasource> datasourceType)
	{
		super(entitiesType, datasourceType);
	}

	PermissionFactory ( )
	{
		this(Permission.class, PermissionDatasource.class);
	}

	private Data writePermissionGrantingToDatabase (Identificator<Member> idOfGranter, Identificator<Member> idOfGrantee, Identificator<Action> idOfAction)
	{
		PermissionDatasource datasource = this.getDefaultDatasource( );
		return datasource.grantPermission(idOfGranter.longValue( ), idOfGrantee.longValue( ), idOfAction.longValue( ));
	}

	private Data writePermissionRevokingToDatabase (Identificator<Member> idOfGranter, Identificator<Permission> idOfPermissionToBeRevoked)
	{
		PermissionDatasource datasource = this.getDefaultDatasource( );
		return datasource.revokePermission(idOfGranter.longValue( ), idOfPermissionToBeRevoked.longValue( ));
	}

	public Permission newInstance (Member grantor, Member grantee, Action actionToGrant)
	throws AccessRestrictedException
	{
		if (!grantor.isPermited(Action.getInstance("GRANT_PERMISSION")))
		{
			throw new AccessRestrictedException (grantor, Action.getInstance("GRANT_PERMISSION"));
		}
		if (!grantor.isPermited(actionToGrant))
		{
			/*
			 * One can't grant action one doesn't have.
			 */ 
			throw new AccessRestrictedException (grantor, actionToGrant);
		}
		Data data =  this.writePermissionGrantingToDatabase(grantor.getId( ), grantee.getId( ), actionToGrant.getId( ));
		if (data == null)
		{
			return null;
		}
		Long idValue = data.<Long>get(Long.class, "id");
		Identificator<Permission> id = new Identificator<Permission> (idValue);
		Date since = data.<Date>get(Date.class, "since");
		Boolean valid = Boolean.valueOf(data.<String>get(String.class, "valid"));
		return new Permission (id, grantor, grantee, actionToGrant, since, valid);
	}

	public RevokedPermission newRevokedInstance (Member revoker, Permission permissionToRevoke)
	throws AccessRestrictedException
	{
		if (!revoker.isPermited(Action.getInstance("REVOKE_PERMISSION")))
		{
			throw new AccessRestrictedException (revoker, Action.getInstance("REVOKE_PERMISSION"));
		}
		if (!revoker.isPermited(permissionToRevoke.getAction( )))
		{
			/*
			 * One can't revoke action one doesn't have.
			 */ 
			throw new AccessRestrictedException (revoker, permissionToRevoke.getAction( ));
		}
		if (!permissionToRevoke.isValid( ))
		{
			if (permissionToRevoke instanceof RevokedPermission)
			{
				return (RevokedPermission)permissionToRevoke;
			} else {
				return new RevokedPermission (revoker, permissionToRevoke, new Date ( ));
			}
		}
		Data data =  this.writePermissionRevokingToDatabase(revoker.getId( ), permissionToRevoke.getId( ));
		if (data == null)
		{
			return null;
		}
		Date until = data.<Date>get(Date.class, "until");
		return new RevokedPermission (revoker, permissionToRevoke, until);
	}

	private Data readFromDatabase (Identificator<Member> idOfGrantee, Identificator<Action> idOfAction)
	{
		PermissionDatasource datasource = this.getDefaultDatasource( );
		return datasource.read(idOfGrantee.longValue( ), idOfAction.longValue( ));
	}

	public Permission getInstance (Data data)
	{
		Identificator<Permission> id = new Identificator<Permission> (data.<Long>get(Long.class, "id"));
		Identificator<Action> idOfAction = new Identificator<Action> (data.<Long>get(Long.class, "action_id"));
		Action action = Action.getInstance(idOfAction);
		Identificator<Member> idOfGranter = new Identificator<Member> (data.<Long>get(Long.class, "grantor_id"));
		Member granter = Member.getInstance(idOfGranter);
		Identificator<Member> idOfGrantee = new Identificator<Member> (data.<Long>get(Long.class, "grantee_id"));
		Member grantee = Member.getInstance(idOfGrantee);
		Date since = data.<Date>get(Date.class, "since");
		Boolean valid = Boolean.valueOf(data.<String>get(String.class, "valid"));
		Permission permission = new Permission (id, granter, grantee, action, since, valid);
		if (!valid)
		{
			Identificator<Member> idOfRevoker = new Identificator<Member> (data.<Long>get(Long.class, "revoker_id"));
			Member revoker = Member.getInstance(idOfRevoker);
			Date until = data.<Date>get(Date.class, "until");
			permission = new RevokedPermission (revoker, permission, until);
		}
		return permission;
	}

	public Permission getInstance (Member grantee, Action permitedAction)
	throws AccessRestrictedException
	{
		Data data = this.readFromDatabase(grantee.getId( ), permitedAction.getId( ));
		if (data == null)
		{
			return null;
		}
		return this.getInstance(data);
	}
}