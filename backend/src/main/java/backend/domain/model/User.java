package backend.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class User {
    private String id;
    private String email;
    private String firstname;
    private String lastname;
    private String phoneNumber;


}
