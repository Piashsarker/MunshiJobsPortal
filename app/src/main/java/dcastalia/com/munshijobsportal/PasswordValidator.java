package dcastalia.com.munshijobsportal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PT on 3/15/2017.
 */

public class PasswordValidator {
    private Pattern pattern;
    private Matcher matcher;

    private static final String PASSWORD_PATTERN =
            "((?=.*[A-Z]).{6,20})";

    public PasswordValidator(){
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    /**
     * Validate password with regular expression
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public boolean validate(final String password){

        matcher = pattern.matcher(password);
        return matcher.matches();

    }
}
