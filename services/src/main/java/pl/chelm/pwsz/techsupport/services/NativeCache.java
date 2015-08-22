package pl.chelm.pwsz.techsupport.services;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

final class NativeCache<T extends Object & Identifiable<T>>
extends Object
implements Cache<T>
{
	NativeCache ( ) {}

	private final Set<T> cache = Collections.<T>synchronizedSet(new HashSet<T> ( ));

	@Override
	public void put (T objectToCache)
	{
		this.cache.add(objectToCache);
	}

	@Override
	public T get (Identificator<T> id)
	{
		for (T cachedObject : this.cache)
		{
			if (cachedObject.getId( ).equals(id))
			{
				return cachedObject;
			}
		}
		return null;
	}
}