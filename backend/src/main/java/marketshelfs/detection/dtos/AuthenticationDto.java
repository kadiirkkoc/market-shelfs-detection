package marketshelfs.detection.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticationDto {
    private String username;
    private String password;
}
