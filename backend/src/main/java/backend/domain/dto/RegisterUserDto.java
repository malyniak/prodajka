package backend.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class RegisterUserDto {
    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 5)
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    @Size(min = 5)
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassword;

    @NotBlank(message = "Ім'я не може бути порожнім")
    private String firstname;

    private String lastname;

    @NotBlank(message = "Телефон обов’язковий")
    @Pattern(
            regexp = "^\\+\\d{1,3}\\d{9,12}$",
            message = "Невірний формат номера телефону. Приклад: + номер країни і основний номер"
    )
    private String phoneNumber;
}
