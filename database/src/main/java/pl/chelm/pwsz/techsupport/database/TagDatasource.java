package pl.chelm.pwsz.techsupport.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Types;

import java.util.Map;

public final class TagDatasource
extends NativeDatasource
implements Datasource
{
	TagDatasource (QueryManager queryManager) 
	{
		super(queryManager);
	}

	public int create (String title)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL tag_create (?, ?)}");
			statement.setString(1, title);
			statement.registerOutParameter(2, Types.INTEGER);
			return statement.getInt(2);
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to create a new tag.", e);
		}
	}

	public Data read (int idOfTag)
	{
		try
		{
			PreparedStatement statement = this.prepareStatement ("SELECT * FROM view_tag WHERE id = ? LIMIT 1;");
			statement.setInt(1, idOfTag);
			return DataFactory.getFirstRow(statement.executeQuery( ));
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to fetch a tag from the database by it's key.", e);
		}
	}

	public Data read (String title)
	{
		try
		{
			PreparedStatement statement = this.prepareStatement ("SELECT * FROM view_tag WHERE LOWER(title) = LOWER(?) LIMIT 1;");
			statement.setString(1, title);
			return DataFactory.getFirstRow(statement.executeQuery( ));
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to fetch a tag from the database by it's title.", e);
		}
	}
}
