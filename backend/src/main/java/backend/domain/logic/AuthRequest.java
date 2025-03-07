package backend.domain.logic;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthRequest {
    String email;
    String password;
}
