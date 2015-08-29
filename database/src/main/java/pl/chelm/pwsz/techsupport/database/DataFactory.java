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
		Data data = new Data ( );
		while (columnsLeft > 0)
		{
			data.put(metaData.getColumnLabel(columnsLeft), resultSet.getObject(columnsLeft));
			columnsLeft--;
		}
		return data;
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
			Data data = new Data ( );
			columnsLeft = metaData.getColumnCount( );
			while (columnsLeft > 0)
			{
				data.put(metaData.getColumnLabel(columnsLeft), resultSet.getObject(columnsLeft));
				columnsLeft--;
			}
			collectionOfData.add(data);
		}
		return collectionOfData;
	}

	private DataFactory ( ) {}
}