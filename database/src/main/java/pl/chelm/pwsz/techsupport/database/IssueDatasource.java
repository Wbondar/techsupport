package pl.chelm.pwsz.techsupport.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Types;

import java.util.Map;

public final class IssueDatasource
extends NativeDatasource
implements Datasource
{
	IssueDatasource (QueryManager queryManager) 
	{
		super(queryManager);
	}

	public int create (int idOfMemberThatIsCreator, String title, String description)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL issue_create (?, ?, ?, ?)}");
			statement.setInt(1, idOfMemberThatIsCreator);
			statement.setString(2, title);
			statement.setString(3, description);
			statement.registerOutParameter(4, Types.INTEGER);
			return statement.getInt(4);
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to create a new issue.", e);
		}
	}

	public Data read (int idOfIssue)
	{
		try
		{
			PreparedStatement statement = this.prepareStatement ("SELECT * FROM view_issue WHERE id = ?;");
			statement.setInt(1, idOfIssue);
			return DataFactory.getFirstRow(statement.executeQuery( ));
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to fetch an issue from the database by it's key.", e);
		}
	}

	public void updateTag (int idOfIssue, int idOfMember, int idOfTag, boolean isAssigned)
	{
		if (isAssigned)
		{
			this.assignTag(idOfIssue, idOfMember, idOfTag);
		} else {
			this.unassignTag(idOfIssue, idOfMember, idOfTag);
		}
	}

	public void assignTag (int idOfIssue, int idOfMember, int idOfTag)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL issue_update_assign_tag (?, ?, ?)}");
			statement.setInt(1, idOfIssue);
			statement.setInt(2, idOfMember);
			statement.setInt(3, idOfTag);
			statement.executeQuery( );
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to write to the database an issue tag assignation.", e);
		}
	}

	public void unassignTag (int idOfIssue, int idOfMember, int idOfTag)
	{
		try
		{
			CallableStatement statement = this.prepareCall ("{CALL issue_update_unassign_tag (?, ?, ?)}");
			statement.setInt(1, idOfIssue);
			statement.setInt(2, idOfMember);
			statement.setInt(3, idOfTag);
			statement.executeQuery( );
		} catch (SQLException e) {
			throw new DatasourceException ("Failed to write to the database an issue tag unassignation.", e);
		}
	}
}
