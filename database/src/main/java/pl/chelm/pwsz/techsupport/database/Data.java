package pl.chelm.pwsz.techsupport.database;

import java.util.Map;

public final class Data
extends Object
{
	private final Map<String, Object> mapping;

	Data (Map<String, Object> mapping)
	{
		this.mapping = mapping;
	}

	public <T> T get (Class<T> type, String label)
	{
		return type.cast(this.mapping.get(label));
	}
}