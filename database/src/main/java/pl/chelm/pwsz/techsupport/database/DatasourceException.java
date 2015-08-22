package pl.chelm.pwsz.techsupport.database;

public final class DatasourceException
extends RuntimeException
{
	public DatasourceException (String message, Throwable cause)
	{
		super(message, cause);
	}
}