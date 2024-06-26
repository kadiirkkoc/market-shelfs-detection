package marketshelfs.detection.dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CorporateUserDto {

    private String marketName;
    private String branchNumber;
    private String phone;
    private String password;
    private int dailyLimit;
}
