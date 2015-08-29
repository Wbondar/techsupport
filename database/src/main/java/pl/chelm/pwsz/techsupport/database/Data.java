package pl.chelm.pwsz.techsupport.database;

import java.util.Map;
import java.util.HashMap; 

public final class Data
extends Object
{
	private final Map<String, Object> mapping;

	Data ( )
	{
		this.mapping = new HashMap<String, Object> ( );
	}

	<T> T put (String label, Class<T> type, T object)
	{
		return type.cast(this.mapping.put(label, type.cast(object)));
	}

	Object put (String label, Object object)
	{
		return this.mapping.put(label, object);
	}

	public <T> T get (Class<T> type, String label)
	{
		return type.cast(this.mapping.get(label));
	}
}