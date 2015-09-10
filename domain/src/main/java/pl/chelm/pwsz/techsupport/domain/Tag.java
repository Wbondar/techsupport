package pl.chelm.pwsz.techsupport.domain;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class Tag
extends Object
implements Identifiable<Tag>
{
	public static Tag newInstance (String title)
	{
		TagFactory factory = Factories.<TagFactory>getInstance(TagFactory.class);
		return factory.newInstance(title);
	}

	public static Tag getInstance (String title)
	{
		TagFactory factory = Factories.<TagFactory>getInstance(TagFactory.class);
		return factory.getInstance(title);
	}

	public static Tag getInstance (Identificator<Tag> id)
	{
		TagFactory factory = Factories.<TagFactory>getInstance(TagFactory.class);
		return factory.getInstance(id);
	}

	public static Tag getInstance (Data data)
	{
		TagFactory factory = Factories.<TagFactory>getInstance(TagFactory.class);
		return factory.getInstance(data);
	}

	private final String title;

	Tag (long id, String title)
	{
		this(new Identificator<Tag> (id), title);
	}

	Tag (Identificator<Tag> id, String title)
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