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

	private void writePermissionGrantingToDatabase (Identificator<Member> idOfAssigner, Identificator<Member> idOfAssignee, Identificator<Member> idOfPermission)
	{
		PermissionDatasource datasource = this.getDefaultDatasource( );
		datasource.grantPermission(ifOfAssigner.longValue( ), idOfAssignee.longValue( ), idOfPermission.longValue( ));
	}

	private void writePermissionRevokingToDatabase (Identificator<Member> idOfAssigner, Identificator<Member> idOfAssignee, Identificator<Member> idOfPermission)
	{
		PermissionDatasource datasource = this.getDefaultDatasource( );
		datasource.revokePermission(ifOfAssigner.longValue( ), idOfAssignee.longValue( ), idOfPermission.longValue( ));
	}

	public Permission newInstance (Member grantor, Member grantee, Permission actionToGrant)
	throws AccessRestrictedException
	{
		if (!grantor.isPermitted(Action.getInstance("GRANT_PERMISSION")))
		{
			throw new AccessRestrictedException (grantor, Action.getInstance("GRANT_PERMISSION"));
		}
		if (!grantor.isPermitted(actionToGrant))
		{
			/*
			 * One can't grant action one doesn't have.
			 */ 
			throw new AccessRestrictedException (grantor, actionToGrant);
		}
		try
		{
			this.writePermissionGrantingToDatabase(grantor.getId( ), asignee.getId( ), action.getId( ));
		} catch (DatasourceException e) {
			throw e;
		}
		return new Permission (grantor, grantee, actionToGrant, new Date ( ), true);
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
		Identificator<Member> idOfGranter = new Identificator<Member> (data.<Long>get(Long.class, "granter_id"));
		Member granter = Member.getInstance(idOfGranter);
		Identificator<Member> idOfGrantee = new Identificator<Member> (data.<Long>get(Long.class, "grantee_id"));
		Member grantee = Member.getInstance(idOfGrantee);
		Date since = data.<Date>get(Date.class, "since");
		Boolean valid = data.<Boolean>get(Boolean.class, "valid");
		Permission permission = new Permission (id, granter, grantee, action, valid);
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