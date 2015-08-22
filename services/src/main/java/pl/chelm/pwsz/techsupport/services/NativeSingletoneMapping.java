package pl.chelm.pwsz.techsupport.services;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

final class NativeSingletoneMapping
extends Object
implements SingletoneMapping
{
	NativeSingletoneMapping ( ) {}

	private final Map<Class<?>, Object> mapping = Collections.<Class<?>, Object>synchronizedMap(new HashMap<Class<?>, Object> ( ));

	@Override
	public <T> void put (Class<T> typeOfObjectToBeStored, T objectToBeStored)
	{
		this.mapping.put(typeOfObjectToBeStored, (T)objectToBeStored);
	}

	@Override
	public <T> T get (Class<T> typeOfDesiredObject)
	{
		return (T)this.mapping.get(typeOfDesiredObject);
	}
}