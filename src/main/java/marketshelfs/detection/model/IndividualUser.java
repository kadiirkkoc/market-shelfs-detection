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
@Table(name = "individual_users")
public class IndividualUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true)
    private String uuid;

    @Column(name = "first_name", unique = true)
    private String firstName;

    @Column(name = "last_name", unique = true)
    private String lastName;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "password", unique = true)
    private String password;

    @Column(name = "daily_limit", unique = true)
    private int dailyLimit;
}
