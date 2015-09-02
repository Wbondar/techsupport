package pl.chelm.pwsz.techsupport.services;

public final class Identificator<T>
extends Number
{
	/**
	 * MySQL casts MEDIUMINT UNSIGNED, INTEGER UNSIGNED and BIGINT
	 * to java.lang.Long hence long primitive type usage.
	 */
	private final long value;

	public Identificator (long value)
	{
		this.value = value;
	}

	public Identificator (Long value)
	{
		this(value.longValue( ));
	}

	public Identificator (int value)
	{
		this((long)value);
	}

	public Identificator (String value)
	{
		this(Long.parseLong(value));
	}

	@Override
	public byte byteValue ( )
	{
		return Long.valueOf(this.value).byteValue( );
	}

	@Override
	public double doubleValue ( )
	{
		return (double)this.value;
	}

	@Override
	public float floatValue ( )
	{
		return (float)this.value;
	}

	@Override
	public int intValue ( )
	{
		return (int)this.value;
	}

	@Override
	public long longValue ( )
	{
		return this.value;
	}

	@Override
	public short shortValue ( )
	{
		return (short)this.value;
	}

	@Override
	public boolean equals (Object o)
	{
		if (o == null)
		{
			return false;
		}
		if (o instanceof Identificator)
		{
			return (this.hashCode( ) == o.hashCode( ));
		}
		return false;
	}

	/* TODO: Override hashCode method properly. */

	@Override
	public int hashCode ( )
	{
		return Long.valueOf(this.value).intValue( );
	}

	@Override
	public String toString ( )
	{
		return Long.toString(this.value);
	}
}