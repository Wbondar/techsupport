package pl.chelm.pwsz.techsupport.domain;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class Tag
extends Object
implements Identifiable<Tag>
{
	private static void cache (Tag tag)
	{
		Cache<Tag> cache = CacheFactory.<Tag>getInstance(Tag.class);
		cache.put(tag);
	}

	private static Tag readFromCache (Identificator<Tag> id)
	{
		Cache<Tag> cache = CacheFactory.<Tag>getInstance(Tag.class);
		return cache.get(id);
	}

	private static long writeToDatabase (String title)
	{
		TagDatasource datasource = DatasourceFactory.<TagDatasource>getInstance(TagDatasource.class);
		return datasource.create(title);
	}

	private static Data readFromDatabase (String title)
	{
		TagDatasource datasource = DatasourceFactory.<TagDatasource>getInstance(TagDatasource.class);
		return datasource.read(title);
	}

	private static Data readFromDatabase (Identificator<Tag> id)
	{
		TagDatasource datasource = DatasourceFactory.<TagDatasource>getInstance(TagDatasource.class);
		return datasource.read(id.longValue( ));
	}

	private static Tag newInstance (String title)
	{
		long idValue = Tag.writeToDatabase(title);
		Identificator<Tag> id = new Identificator<Tag> (idValue);
		Tag tag = new Tag (id, title);
		Tag.cache(tag);
		return tag;
	}

	public static Tag getInstance (String title)
	{
		Tag tag = null;
		Data data = Tag.readFromDatabase(title);
		if (data != null)
		{
			Identificator<Tag> id = new Identificator<Tag> (data.<Long>get(Long.class, "id"));
			tag = Tag.readFromCache(id);
			if (tag == null)
			{
				tag = new Tag (id, title);
				Tag.cache(tag);
			}
		} else {
			tag = Tag.newInstance(title);
		}
		return tag;
	}

	public static Tag getInstance (Identificator<Tag> id)
	{
		Tag tag = Tag.readFromCache(id);
		if (tag == null)
		{
			Data data = Tag.readFromDatabase(id);
			if (data == null)
			{
				return null;
			}
			return Tag.getInstance(data);
		}
		return tag;
	}

	public static Tag getInstance (Data data)
	{
		Identificator<Tag> id = new Identificator<Tag> (data.<Long>get(Long.class, "id"));
		Tag tag = Tag.readFromCache(id);
		if (tag == null)
		{
			String title = data.<String>get(String.class, "title");
			tag = new Tag (id, title);
			Tag.cache(tag);
		}
		return tag;
	}

	private final String title;

	private Tag (long id, String title)
	{
		this(new Identificator<Tag> (id), title);
	}

	private Tag (Identificator<Tag> id, String title)
	{
		this.id    = id;
		this.title = title;
	}

	public String getTitle ( )
	{
		return this.title;
	}

	private final Identificator<Tag> id;

	public Identificator<Tag> getId ( )
	{
		return this.id;
	}

	@Override
	public int hashCode ( )
	{
		return this.id.intValue( );
	}
}