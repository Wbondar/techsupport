package pl.chelm.pwsz.techsupport.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Types;

import java.util.Collection;
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
			PreparedStatement statement = this.prepareStatement ("SELECT * FROM view_permission WHERE id = CONCAT(?, ?) LIMIT 1;");
			statement.setLong(1, idOfGrantee);
			statement.setLong(2, idOfAction);
			DataFactory.getFirstRow(statement.executeQuery( ));
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
			DataFactory.getFirstRow(statement.executeQuery( ));
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

	public void grantPermission (long idOfGranter, long idOfGrantee, long idOfAction)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL member_update_permission_grant (?, ?, ?)}");
			statement.setLong(1, idOfGranter);
			statement.setLong(2, idOfGrantee);
			statement.setLong(3, idOfAction);
			statement.executeQuery( );
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to write to the database a member permission assignation.", e);
		}
	}

	public void revokePermission (long idOfGranter, long idOfGrantee, long idOfAction)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL member_update_permission_revoke (?, ?, ?)}");
			statement.setLong(1, idOfGranter);
			statement.setLong(2, idOfGrantee);
			statement.setLong(3, idOfAction);
			statement.executeQuery( );
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to write to the database a member permission unassignation.", e);
		}
	}
}
