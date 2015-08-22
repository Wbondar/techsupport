package pl.chelm.pwsz.techsupport.services;

public interface Cache<T extends Object & Identifiable<T>>
{
	public abstract void put (T objectToCache);

	public abstract T get (Identificator<T> id);
}