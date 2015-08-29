package pl.chelm.pwsz.techsupport.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Types;

import java.util.Map;
import java.util.Collection;

public final class IssueDatasource
extends NativeDatasource
implements Datasource
{
	IssueDatasource (QueryManager queryManager) 
	{
		super(queryManager);
	}

	public long create (long idOfMemberThatIsCreator, String title, String description)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL issue_create (?, ?, ?, ?)}");
			statement.setLong(1, idOfMemberThatIsCreator);
			statement.setString(2, title);
			statement.setString(3, description);
			statement.registerOutParameter(4, Types.INTEGER);
			statement.execute( );
			long id = statement.getLong(4);
			if (id < 1)
			{
				id = (long) statement.getInt(4);
			}
			return id;
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to create a new issue.", e);
		}
	}

	public Data read (long idOfIssue)
	{
		try
		{
			PreparedStatement statement = this.prepareStatement ("SELECT * FROM view_issue WHERE id = ? LIMIT 1;");
			statement.setLong(1, idOfIssue);
			return DataFactory.getFirstRow(statement.executeQuery( ));
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to fetch an issue from the database by it's key.", e);
		}
	}

	public Collection<Data> readByIssuer (long idOfIssuer)
	{
		try
		{
			PreparedStatement statement = this.prepareStatement ("SELECT * FROM view_issue WHERE issuer_id = ?;");
			statement.setLong(1, idOfIssuer);
			return DataFactory.getAllRows(statement.executeQuery( ));
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to fetch an issue from the database by it's issuer.", e);
		}
	}

	public void updateTag (long idOfIssue, long idOfMember, long idOfTag, boolean isAssigned)
	{
		if (isAssigned)
		{
			this.assignTag(idOfIssue, idOfMember, idOfTag);
		} else {
			this.unassignTag(idOfIssue, idOfMember, idOfTag);
		}
	}

	public void assignTag (long idOfIssue, long idOfMember, long idOfTag)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL issue_update_tag_assign (?, ?, ?)}");
			statement.setLong(1, idOfMember);
			statement.setLong(2, idOfIssue);
			statement.setLong(3, idOfTag);
			statement.executeQuery( );
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to write to the database an issue tag assignation.", e);
		}
	}

	public void unassignTag (long idOfIssue, long idOfMember, long idOfTag)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL issue_update_tag_unassign (?, ?, ?)}");
			statement.setLong(1, idOfMember);
			statement.setLong(2, idOfIssue);
			statement.setLong(3, idOfTag);
			statement.executeQuery( );
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to write to the database an issue tag unassignation.", e);
		}
	}
}
