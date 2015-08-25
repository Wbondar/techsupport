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
		int columnsLeft = metaData.getColumnCount( );
		if (columnsLeft < 1)
		{
			return null;
		}
		resultSet.beforeFirst( );
		if (!resultSet.isBeforeFirst( ))
		{
			return null;
		}
		resultSet.next( );
		Map<String, Object> mapping = new HashMap<String, Object> ( );
		while (columnsLeft > 0)
		{
			mapping.put(metaData.getColumnLabel(columnsLeft), resultSet.getObject(columnsLeft));
			columnsLeft--;
		}
		return new Data (mapping);
	}

	public static Collection<Data> getAllRows (ResultSet resultSet)
	throws SQLException
	{
		ResultSetMetaData metaData = resultSet.getMetaData( );
		int columnsLeft = metaData.getColumnCount( );
		if (columnsLeft < 1)
		{
			return null;
		}
		resultSet.beforeFirst( );
		if (!resultSet.isBeforeFirst( ))
		{
			return null;
		}
		List<Data> collectionOfData = new ArrayList<Data> ( );
		while (resultSet.next( ))
		{
			Map<String, Object> mapping = new HashMap<String, Object> ( );
			while (columnsLeft > 0)
			{
				mapping.put(metaData.getColumnLabel(columnsLeft), resultSet.getObject(columnsLeft));
				columnsLeft--;
			}
			Data data = new Data (mapping);
			collectionOfData.add(data);
		}
		return collectionOfData;
	}

	private DataFactory ( ) {}
}