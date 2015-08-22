package pl.chelm.pwsz.techsupport.database;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import java.lang.reflect.Constructor;

import pl.chelm.pwsz.techsupport.services.*;

public final class DatasourceFactory
extends Object
{
	private static final SingletoneMapping mapping = SingletoneMappingFactory.newInstance ( );

	public static <T extends Object & Datasource> T getInstance (Class<T> typeOfADatasource)
	{

		T datasource = DatasourceFactory.mapping.<T>get(typeOfADatasource);
		if (datasource == null)
		{
			try
			{
				Constructor<T> constructor = typeOfADatasource.getDeclaredConstructor(QueryManager.class);
				datasource = constructor.newInstance(NativeQueryManager.INSTANCE);
				DatasourceFactory.mapping.<T>put(typeOfADatasource, datasource);
			} catch (Exception e) {
				throw new DatasourceException ("Failed to get datasource.", e);
			}
		}
		return datasource;
	}

	private DatasourceFactory ( ) {}
}