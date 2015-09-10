package pl.chelm.pwsz.techsupport.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Types;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public final class PermissionDatasource
extends NativeDatasource
implements Datasource
{
	PermissionDatasource (QueryManager queryManager) 
	{
		super(queryManager);
	}

	public Data read (long idOfGrantee, long idOfAction)
	{
		try
		{
			PreparedStatement statement = this.prepareStatement ("SELECT * FROM view_permission WHERE CONCAT(grantee_id, action_id) = CONCAT(?, ?) LIMIT 1;");
			statement.setLong(1, idOfGrantee);
			statement.setLong(2, idOfAction);
			return DataFactory.getFirstRow(statement.executeQuery( ));
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to read a permission from the database.", e);
		}
	}

	public Data read (long id)
	{
		try
		{
			PreparedStatement statement = this.prepareStatement ("SELECT * FROM view_permission WHERE id = ? LIMIT 1;");
			statement.setLong(1, id);
			return DataFactory.getFirstRow(statement.executeQuery( ));
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to read a permission from the database.", e);
		}
	}


	public void update (long idOfGranter, long idOfGrantee, long idOfAction, boolean isValid)
	{
		if (isValid)
		{
			this.grantPermission(idOfGranter, idOfGrantee, idOfAction);
		} else {
			this.revokePermission(idOfGranter, idOfGrantee, idOfAction);
		}
	}

	public Data grantPermission (long idOfGranter, long idOfGrantee, long idOfAction)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL member_update_permission_grant (?, ?, ?, ?, ?, ?)}");
			statement.setLong(1, idOfGranter);
			statement.setLong(2, idOfGrantee);
			statement.setLong(3, idOfAction);
			statement.registerOutParameter(4, Types.INTEGER);
			statement.registerOutParameter(5, Types.TIMESTAMP);
			statement.registerOutParameter(6, Types.BOOLEAN);
			statement.execute( );
			Long id = statement.getLong(4);
			Date since = statement.getDate(5);
			Boolean valid = statement.getBoolean(6);
			Data data = new Data ( );
			data.<Long>put("id", Long.class, id);
			data.<Date>put("since", Date.class, since);
			data.<Boolean>put("valid", Boolean.class, valid);
			return data;
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to write to the database a member permission assignation.", e);
		}
	}

	public Data revokePermission (long idOfRevoker, long idOfPermission)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL member_update_permission_revoke (?, ?, ?)}");
			statement.setLong(1, idOfRevoker);
			statement.setLong(2, idOfPermission);
			statement.registerOutParameter(3, Types.TIMESTAMP);
			statement.execute( );
			Date until = statement.getDate(3);
			Data data = new Data ( );
			data.<Long>put("id", Long.class, idOfPermission);
			data.<Date>put("until", Date.class, until);
			data.<Boolean>put("valid", Boolean.class, false);
			return data;
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to write to the database a member permission unassignation.", e);
		}
	}
}