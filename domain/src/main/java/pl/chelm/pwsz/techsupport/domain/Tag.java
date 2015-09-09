package pl.chelm.pwsz.techsupport.domain;

import pl.chelm.pwsz.techsupport.database.*;
import pl.chelm.pwsz.techsupport.services.*;

public final class Tag
extends Object
implements Identifiable<Tag>
{
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