package pl.chelm.pwsz.techsupport.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import java.util.ResourceBundle;

enum NativeConnectionFactory
implements ConnectionFactory
{
	/* TODO: Ensure that database credentials are properly protected. */
	
	INSTANCE ( );

	private final String dbURL;
	private final String username;
	private final String password;

	private NativeConnectionFactory ( )
	{
		ResourceBundle resourceBundle = ResourceBundle.getBundle("DatabaseCredentials");
		this.username = resourceBundle.getString("USERNAME");
		this.password = resourceBundle.getString("PASSWORD");
		this.dbURL    = "jdbc:mysql://" + resourceBundle.getString("HOST") + ":" + resourceBundle.getString("PORT") + "/" + resourceBundle.getString("DATABASE");
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