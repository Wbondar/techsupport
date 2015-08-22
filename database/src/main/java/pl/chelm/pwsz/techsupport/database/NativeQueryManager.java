package pl.chelm.pwsz.techsupport.database;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

enum NativeQueryManager
implements QueryManager
{
	INSTANCE (NativeConnectionFactory.INSTANCE);

	private final ConnectionFactory connectionFactory;

	private NativeQueryManager (ConnectionFactory connectionFactory)
	{
		this.connectionFactory = connectionFactory;
	}

	private Connection getConnection ( )
	throws SQLException
	{
		return this.connectionFactory.getConnection( );
	}

	private final Map<String, PreparedStatement> preparedStatements = Collections.<String, PreparedStatement>synchronizedMap(new HashMap<String, PreparedStatement> ( ));

	@Override
	public CallableStatement prepareCall (String sql)
	throws SQLException
	{
		Connection connection = this.getConnection( );
		CallableStatement statement = (CallableStatement)this.preparedStatements.get(sql);
		if (statement == null || statement.isClosed( ))
		{
			statement = connection.prepareCall(sql);
			this.preparedStatements.put(sql, (PreparedStatement)statement);
		}
		return statement;
	}

	@Override
	public PreparedStatement prepareStatement (String sql)
	throws SQLException
	{
		Connection connection = this.getConnection( );
		PreparedStatement statement = this.preparedStatements.get(sql);
		if (statement == null || statement.isClosed( ))
		{
			statement = connection.prepareStatement(sql);
			this.preparedStatements.put(sql, statement);
		}
		return statement;
	}
}