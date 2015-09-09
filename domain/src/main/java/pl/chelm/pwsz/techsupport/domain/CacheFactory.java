package pl.chelm.pwsz.techsupport.domain;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

abstract class CacheFactory<T, D extends Object & Datasoruce>
extends Object
implements Factory<T extends Object & Identifiable<T>>
{
	private final Class<T> type;

	CacheFactory (Class<T> typeOfEntitiesToBeProduced, Class<D> typeOfDefaultDatasource)
	{
		this.typeOfEntitiesToBeProduced = typeOfEntitiesToBeProduced;
		this.typeOfDefaultDatasource    = typeOfDefaultDatasource;
	}

	private final Cache<T> getCache ( )
	{
		return CacheFactory.<T>getInstance(this.typeOfEntitiesToBeProduced);
	}

	protected final void cache (T objectToCache) 
	{
		this.getCache( ).put(objectToCache);
	}

	protected final T readFromCache (Identificator<T> id)
	{
		return this.getCache( ).get(id);
	}

	protected final <S extends Object & Datasource> S getDatasoutrce (Class<S> typeOfDatasource)
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