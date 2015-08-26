package pl.chelm.pwsz.techsupport.services;

public final class Identificator<T>
extends Number
{
	private final int value;

	public Identificator (int value)
	{
		this.value = value;
	}

	public Identificator (String value)
	{
		this(Integer.parseInt(value));
	}

	@Override
	public byte byteValue ( )
	{
		return (byte)this.value;
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
		return this.value;
	}

	@Override
	public long longValue ( )
	{
		return (long)this.value;
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

	@Override
	public int hashCode ( )
	{
		return this.value;
	}

	@Override
	public String toString ( )
	{
		return Integer.toString(this.value);
	}
}