package pl.chelm.pwsz.techsupport.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Types;

import java.util.Map;

public final class MemberDatasource
extends NativeDatasource
implements Datasource
{
	MemberDatasource (QueryManager queryManager) 
	{
		super(queryManager);
	}

	public int create (String name, String password)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL member_create (?, ?, ?)}");
			statement.setString(1, name);
			statement.setString(2, password);
			statement.registerOutParameter(3, Types.INTEGER);
			return statement.getInt(3);
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to create a new member.", e);
		}
	}

	public Data read (int idOfMember)
	{
		try
		{
			PreparedStatement statement = this.prepareStatement ("SELECT * FROM view_member WHERE id = ? LIMIT 1;");
			statement.setInt(1, idOfMember);
			return DataFactory.getFirstRow(statement.executeQuery( ));
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to fetch a member from the database by their's key.", e);
		}
	}

	public Data read (String name, String password)
	{
		try
		{
			PreparedStatement statement = this.prepareStatement ("SELECT * FROM view_member WHERE LOWER(CONCAT(name, password)) = LOWER(CONCAT(?, ?)) LIMIT 1;");
			statement.setString(1, name);
			statement.setString(2, password);
			return DataFactory.getFirstRow(statement.executeQuery( ));
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to fetch a member from the database by their name.", e);
		}
	}
}
