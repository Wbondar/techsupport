package pl.chelm.pwsz.techsupport.services;

import de.rtner.security.auth.spi.SimplePBKDF2;

public final class PasswordHandler
extends Object
{
    public static String hash (String password)
    {
        return (new SimplePBKDF2 ( )).deriveKeyFormatted(password);
    }

    public static boolean validate (String password, String hash)
    {
        return (new SimplePBKDF2 ( )).verifyKeyFormatted(hash, password);
    }

    private PasswordHandler ( ) {}
}