package pl.chelm.pwsz.techsupport.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Types;

import java.util.Map;

import pl.chelm.pwsz.techsupport.services.PasswordHandler;

public final class MemberDatasource
extends NativeDatasource
implements Datasource
{
	MemberDatasource (QueryManager queryManager) 
	{
		super(queryManager);
	}

	public long create (String name, String password)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL member_create (?, ?, ?)}");
			statement.setString(1, name);
			statement.setString(2, PasswordHandler.hash(password));
			statement.registerOutParameter(3, Types.INTEGER);
			statement.execute( );
			long id = statement.getLong(3);
			if (id < 1)
			{
				id = (long) statement.getInt(3);
			}
			return id;
		} catch (Exception e) {
			throw new DatasourceException ("Failed to create a new member.", e);
		}
	}

	public Data read (long idOfMember)
	{
		try
		{
			PreparedStatement statement = this.prepareStatement ("SELECT * FROM view_member WHERE id = ? LIMIT 1;");
			statement.setLong(1, idOfMember);
			return DataFactory.getFirstRow(statement.executeQuery( ));
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to fetch a member from the database by their's key.", e);
		}
	}

	public Data read (String name, String password)
	{
		try
		{
			PreparedStatement statement = this.prepareStatement ("SELECT * FROM view_member WHERE name = LOWER(?) LIMIT 1;");
			statement.setString(1, name);
			Data data = DataFactory.getFirstRow(statement.executeQuery( ));
			if (data == null)
			{
				return null;
			}
			String storedPasswordHash = data.<String>get(String.class, "password_hash");
			if (PasswordHandler.validate(password, storedPasswordHash) == true)
			{	
				return data;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new DatasourceException ("Failed to fetch a member from the database by their name and password.", e);
		}
	}
}
