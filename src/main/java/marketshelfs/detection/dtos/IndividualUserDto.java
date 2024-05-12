package marketshelfs.detection.dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class IndividualUserDto {

    private String firstName;
    private String lastName;
    private String username;
    private String phone;
    private String password;
}
