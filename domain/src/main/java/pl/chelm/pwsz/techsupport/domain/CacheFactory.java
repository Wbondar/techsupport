package pl.chelm.pwsz.techsupport.domain;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

abstract class CacheFactory<T extends Identifiable<T>, D extends Datasource>
extends Object
implements Factory<T>
{
	private final Class<T> typeOfEntitiesToBeProduced;

	CacheFactory (Class<T> typeOfEntitiesToBeProduced, Class<D> typeOfDefaultDatasource)
	{
		this.typeOfEntitiesToBeProduced = typeOfEntitiesToBeProduced;
		this.typeOfDefaultDatasource    = typeOfDefaultDatasource;
	}

	private final Cache<T> getCache ( )
	{
		return pl.chelm.pwsz.techsupport.services.CacheFactory.<T>getInstance(this.typeOfEntitiesToBeProduced);
	}

	protected final void cache (T objectToCache) 
	{
		this.getCache( ).put(objectToCache);
	}

	protected final T readFromCache (Identificator<T> id)
	{
		return this.getCache( ).get(id);
	}

	protected final <S extends Object & Datasource> S getDatasource (Class<S> typeOfDatasource)
	{
		return DatasourceFactory.<S>getInstance(typeOfDatasource);
	}

	private final Class<D> typeOfDefaultDatasource;

	protected final D getDefaultDatasource ( )
	{
		return this.<D>getDatasource(this.typeOfDefaultDatasource);
	}

	protected final Data readFromDatabase (Identificator<T> id)
	{
		D datasource = this.getDefaultDatasource( );
		return datasource.read(id.longValue( ));
	}

	public T getInstance (Identificator<T> id)
	{
		assert id != null;
		T entity = this.readFromCache(id);
		if (entity == null)
		{
			Data data = this.readFromDatabase(id);
			entity = this.getInstance(data);
			if (entity == null)
			{
				return null;
			}
			this.cache(entity);
		}
		return entity;
	}

	abstract T getInstance (Data data);
}