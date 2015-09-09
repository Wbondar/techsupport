package pl.chelm.pwsz.techsupport.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Types;

import java.util.Collection;
import java.util.Map;

public final class ActionDatasource
extends NativeDatasource
implements Datasource
{
	ActionDatasource (QueryManager queryManager) 
	{
		super(queryManager);
	}

	public long create (String label)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL action_create (?, ?)}");
			statement.setString(1, label);
			statement.registerOutParameter(2, Types.INTEGER);
			statement.execute( );
			return statement.getLong(2);
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to create a new action.", e);
		}
	}

	public Data read (long idOfAction)
	{
		try
		{
			PreparedStatement statement = this.prepareStatement ("SELECT * FROM view_action WHERE id = ? LIMIT 1;");
			statement.setLong(1, idOfAction);
			return DataFactory.getFirstRow(statement.executeQuery( ));
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to fetch an action from the database by it's key.", e);
		}
	}

	public Data read (String label)
	{
		try
		{
			PreparedStatement statement = this.prepareStatement ("SELECT * FROM view_action WHERE LOWER(label) = LOWER(?) LIMIT 1;");
			statement.setString(1, label);
			return DataFactory.getFirstRow(statement.executeQuery( ));
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to fetch an action from the database by it's label.", e);
		}
	}
}
