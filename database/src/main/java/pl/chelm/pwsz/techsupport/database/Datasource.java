package pl.chelm.pwsz.techsupport.database;

/**
 * MySQL primary key integers are unsigned, 
 * Java ints are signed. 
 * Same number of bits, 
 * but the unsigned has a full 4G positive range.
 */

interface Datasource 
{
	public abstract Data read (long id);
}