package backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderImpl {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    public static PasswordEncoder passwordEncoder() {
        return ENCODER;
    }
}
