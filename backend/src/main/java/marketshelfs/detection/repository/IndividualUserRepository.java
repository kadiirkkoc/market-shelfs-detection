package marketshelfs.detection.repository;

import marketshelfs.detection.model.IndividualUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualUserRepository extends JpaRepository<IndividualUser,Long> {
}
