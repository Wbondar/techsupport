package pl.chelm.pwsz.techsupport.database;
	
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Types;

import java.util.Date;

public final class CommentDatasource
extends NativeDatasource
implements Datasource
{
	CommentDatasource (QueryManager queryManager) 
	{
		super(queryManager);
	}

	public Data create (long idOfIssueToCommentOn, long idOfAuthor, String content)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL comment_create (?, ?, ?, ?, ?)}");
			statement.setLong(1, idOfIssueToCommentOn);
			statement.setLong(2, idOfAuthor);
			statement.setString(3, content);
			statement.registerOutParameter(4, Types.INTEGER);
			statement.registerOutParameter(5, Types.DATE);
			statement.execute( );
			long id = statement.getLong(4);
			if (id < 1)
			{
				id = (long) statement.getInt(4);
			}
			Date when = statement.getDate(5);
			Data data = new Data( );
			data.<Long>put("id", Long.class, new Long(id));
			data.<Long>put("author_id", Long.class, new Long(idOfAuthor));
			data.<Long>put("issue_id", Long.class, new Long(idOfIssueToCommentOn));
			data.<String>put("content", String.class, content);
			data.<Date>put("posted_at", Date.class, when);
			return data;
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to write to the database a new comment.", e);
		}
	}

	public Data createResponse (long idOfCommentToRespondeTo, long idOfAuthor, String content)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL response_create (?, ?, ?, ?, ?)}");
			statement.setLong(1, idOfCommentToRespondeTo);
			statement.setLong(2, idOfAuthor);
			statement.setString(3, content);
			statement.registerOutParameter(4, Types.INTEGER);
			statement.registerOutParameter(5, Types.DATE);
			statement.execute( );
			long id = statement.getLong(4);
			if (id < 1)
			{
				id = (long) statement.getInt(4);
			}
			Date when = statement.getDate(5);
			Data data = new Data( );
			data.<Long>put("id", Long.class, new Long(id));
			data.<Long>put("author_id", Long.class, new Long(idOfAuthor));
			data.<Long>put("parent_id", Long.class, new Long(idOfCommentToRespondeTo));
			data.<String>put("content", String.class, content);
			data.<Date>put("posted_at", Date.class, when);
			return data;
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to write to the database a new response to comment.", e);
		}
	}

	public Data read (long idOfComment)
	{
		try
		{
			PreparedStatement statement = this.prepareStatement ("SELECT * FROM view_comment WHERE id = ? LIMIT 1;");
			statement.setLong(1, idOfComment);
			return DataFactory.getFirstRow(statement.executeQuery( ));
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to fetch a comment from the database by it's key.", e);
		}
	}

	public java.util.Collection<Data> readIssue (long idOfIssue)
	{
		try
		{
			PreparedStatement statement = this.prepareStatement ("SELECT * FROM view_comment WHERE issue_id = ?;");
			statement.setLong(1, idOfIssue);
			return DataFactory.getAllRows(statement.executeQuery( ));
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to fetch a comment from the database by it's issue.", e);
		}
	}
}
