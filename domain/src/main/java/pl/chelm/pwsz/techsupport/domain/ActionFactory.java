package pl.chelm.pwsz.techsupport.domain;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

final class ActionFactory
extends CacheFactory<Action, ActionDatasource>
{
	ActionFactory (Class<Action> entitiesType, Class<ActionDatasource> datasourceType)
	{
		super(entitiesType, datasourceType);
	}

	ActionFactory ( )
	{
		this(Action.class, ActionDatasource.class);
	}

	private long writeToDatabase (String label)
	{
		ActionDatasource datasource = this.getDefaultDatasource( );
		return datasource.create(label);
	}

	private Data readFromDatabase (String label)
	{
		ActionDatasource datasource = this.getDefaultDatasource( );
		return datasource.read(label);
	}

	public Action newInstance (String label)
	{
		long idValue = this.writeToDatabase(label);
		Identificator<Action> id = new Identificator<Action> (idValue);
		Action action = new Action (id, label);
		this.cache(action);
		return action;
	}

	public Action getInstance (String label)
	{
		Action action = null;
		Data data = this.readFromDatabase(label);
		if (data != null)
		{
			action = this.getInstance(data);
		}
		return action;
	}

	public Action getInstance (Data data)
	{
		Identificator<Action> id = new Identificator<Action> (data.<Long>get(Long.class, "id"));
		Action action = this.readFromCache(id);
		if (action == null)
		{
			String label = data.<String>get(String.class, "label");
			action = new Action (id, label);
			this.cache(action);
		}
		return action;
	}
}