package backend.logic;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RefreshResponse {
    private final String refreshToken;
}
