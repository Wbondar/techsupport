package pl.chelm.pwsz.techsupport.domain;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

final class TagFactory
extends CacheFactory<Tag, TagDatasource>
{
	TagFactory (Class<Tag> entitiesType, Class<TagDatasource> datasourceType)
	{
		super(entitiesType, datasourceType);
	}

	TagFactory ( )
	{
		this(Tag.class, TagDatasource.class);
	}

	private long writeToDatabase (String title)
	{
		TagDatasource datasource = this.getDefaultDatasource( );
		return datasource.create(title);
	}

	private Data readFromDatabase (String title)
	{
		TagDatasource datasource = this.getDefaultDatasource( );
		return datasource.read(title);
	}

	public Tag newInstance (String title)
	{
		long idValue = this.writeToDatabase(title);
		Identificator<Tag> id = new Identificator<Tag> (idValue);
		Tag tag = new Tag (id, title);
		this.cache(tag);
		return tag;
	}

	public Tag getInstance (String title)
	{
		Tag tag = null;
		Data data = this.readFromDatabase(title);
		if (data != null)
		{
			tag = this.getInstance(data);
		}
		return tag;
	}

	public Tag getInstance (Data data)
	{
		Identificator<Tag> id = new Identificator<Tag> (data.<Long>get(Long.class, "id"));
		Tag tag = this.readFromCache(id);
		if (tag == null)
		{
			String title = data.<String>get(String.class, "title");
			tag = new Tag (id, title);
			this.cache(tag);
		}
		return tag;
	}
}