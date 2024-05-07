package marketshelfs.detection.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "corporate_users")
public class CorporateUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true)
    private String uuid;

    @Column(name = "market_name", unique = true)
    private String marketName;

    @Column(name = "branch_number", unique = true)
    private String branchNumber;

    @Column(name = "password", unique = true)
    private String password;

    @Column(name = "daily_limit", unique = true)
    private int dailyLimit;
}
