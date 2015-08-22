package pl.chelm.pwsz.techsupport.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

final class DataFactory
extends Object
{
	public static Data getFirstRow (ResultSet resultSet)
	throws SQLException
	{
		ResultSetMetaData metaData = resultSet.getMetaData( );
		resultSet.beforeFirst( );
		resultSet.next( );
		Map<String, Object> mapping = new HashMap<String, Object> ( );
		int i = 0;
		for (i = 0; i < metaData.getColumnCount( ); i++)
		{
			mapping.put(metaData.getColumnLabel(i), resultSet.getObject(i));
		}
		return new Data (mapping);
	}

	public static Collection<Data> getAllRows (ResultSet resultSet)
	throws SQLException
	{
		ResultSetMetaData metaData = resultSet.getMetaData( );
		List<Data> collectionOfData = new ArrayList<Data> ( );
		resultSet.beforeFirst( );
		while (resultSet.next( ))
		{
			Map<String, Object> mapping = new HashMap<String, Object> ( );
			int i = 0;
			for (i = 0; i < metaData.getColumnCount( ); i++)
			{
				mapping.put(metaData.getColumnLabel(i), resultSet.getObject(i));
			}
			Data data = new Data (mapping);
			collectionOfData.add(data);
		}
		return collectionOfData;
	}

	private DataFactory ( ) {}
}