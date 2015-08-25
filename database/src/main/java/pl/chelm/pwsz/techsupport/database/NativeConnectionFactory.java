package pl.chelm.pwsz.techsupport.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

enum NativeConnectionFactory
implements ConnectionFactory
{
	/* TODO: Provide proper resource for database credentials handling. */
	
	INSTANCE ("jdbc:mysql://localhost:3306/techsupport", "root", "root");

	private final String dbURL;
	private final String username;
	private final String password;

	private NativeConnectionFactory (String dbURL, String username, String password)
	{
		this.dbURL    = dbURL;
		this.username = username;
		this.password = password;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new AssertionError ("Failed to locate database driver: " + e.getMessage( ), e);
        }
	}

	private Connection connection = null;

	@Override
	public Connection getConnection ( )
	throws SQLException
	{
		if (this.connection == null || this.connection.isClosed( ))
		{
			this.connection = DriverManager.getConnection(this.dbURL, this.username, this.password);
		}
		return connection;
	}
}