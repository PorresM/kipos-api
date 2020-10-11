package fr.mporres.kiposapi.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(11);

    public static void main(String[] args) {
        String p1= "admin";
        String h1 = PASSWORD_ENCODER.encode(p1);
        System.err.println(p1 + " = " + h1);
    }

}
