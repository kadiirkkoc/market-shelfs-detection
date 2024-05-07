package marketshelfs.detection.repository;

import marketshelfs.detection.model.CorporateUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorporateUserRepository extends JpaRepository<CorporateUser,Long> {
}
