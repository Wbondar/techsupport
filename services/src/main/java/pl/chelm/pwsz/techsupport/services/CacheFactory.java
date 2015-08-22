package pl.chelm.pwsz.techsupport.services;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public final class CacheFactory
{
	private static final Map<Class<?>, Object> mapping = Collections.<Class<?>, Object>synchronizedMap(new HashMap<Class<?>, Object> ( ));

	public static <T extends Object & Identifiable<T>> Cache<T> getInstance (Class<T> typeOfObjectsToBeCached)
	{
		Cache<T> cache = (Cache<T>)CacheFactory.mapping.get(typeOfObjectsToBeCached);
		if (cache == null)
		{
			cache = new NativeCache<T> ( );
			CacheFactory.mapping.put(typeOfObjectsToBeCached, (Object)cache);
		}
		return cache;
	}

	private CacheFactory ( ) {}
}