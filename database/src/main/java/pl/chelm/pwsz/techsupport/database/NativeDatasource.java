package pl.chelm.pwsz.techsupport.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

abstract class NativeDatasource
extends Object
implements Datasource
{
	private final QueryManager queryManager;

	NativeDatasource (QueryManager queryManager) 
	{
		this.queryManager = queryManager;
	}

	protected final CallableStatement prepareCall (String sql)
	throws SQLException
	{
		return this.queryManager.prepareCall(sql);
	}

	protected final PreparedStatement prepareStatement (String sql)
	throws SQLException
	{
		return this.queryManager.prepareStatement(sql);
	}
}