package marketshelfs.detection.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import marketshelfs.detection.enums.UserRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token;
    private UserRole userRole;
    private int dailyLimit;
}
