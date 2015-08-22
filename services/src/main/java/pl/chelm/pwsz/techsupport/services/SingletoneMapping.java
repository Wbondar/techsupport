package pl.chelm.pwsz.techsupport.services;

public interface SingletoneMapping
{
	public abstract <T> void put (Class<T> typeOfObjectToBeStored, T objectToBeStored);

	public abstract <T> T get (Class<T> typeOfDesiredObject);
}