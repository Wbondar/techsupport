package pl.chelm.pwsz.techsupport.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Types;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

public final class CommentDatasource
extends NativeDatasource
implements Datasource
{
	CommentDatasource (QueryManager queryManager) 
	{
		super(queryManager);
	}

	public Data create (int idOfIssueToCommentOn, int idOfAuthor, String content)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL comment_create (?, ?, ?, ?, ?)}");
			statement.setInt(1, idOfIssueToCommentOn);
			statement.setInt(2, idOfAuthor);
			statement.setString(3, content);
			statement.registerOutParameter(4, Types.INTEGER);
			statement.registerOutParameter(5, Types.DATE);
			Integer id = statement.getInt(4);
			Date when = statement.getDate(5);
			Map<String, Object> mapping = new HashMap<String, Object> ( );
			mapping.put("id", id);
			mapping.put("when", when);
			return new Data (mapping);
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to create a new comment.", e);
		}
	}

	public Data createResponse (int idOfCOmmentToRespondeTo, int idOfAuthor, String content)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL response_create (?, ?, ?, ?, ?)}");
			statement.setInt(1, idOfCOmmentToRespondeTo);
			statement.setInt(2, idOfAuthor);
			statement.setString(3, content);
			statement.registerOutParameter(4, Types.INTEGER);
			statement.registerOutParameter(5, Types.DATE);
			Integer id = statement.getInt(4);
			Date when = statement.getDate(5);
			Map<String, Object> mapping = new HashMap<String, Object> ( );
			mapping.put("id", id);
			mapping.put("when", when);
			return new Data (mapping);
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to create a new comment.", e);
		}
	}

	public Data read (int idOfComment)
	{
		try
		{
			PreparedStatement statement = this.prepareStatement ("SELECT * FROM view_comment WHERE id = ? LIMIT 1;");
			statement.setInt(1, idOfComment);
			return DataFactory.getFirstRow(statement.executeQuery( ));
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to fetch a comment from the database by it's key.", e);
		}
	}
}
