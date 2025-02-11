package backend.dto;

import backend.util.PasswordEncoderImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterUserDto {
    private String firstname;
    @Nullable
    private String lastname;
    private String email;
    private String phoneNumber;
//    @JsonIgnore
    private String password;

    public void setPassword(String password) {
        this.password = PasswordEncoderImpl.passwordEncoder().encode(password);
    }
}
