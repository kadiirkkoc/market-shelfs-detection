package marketshelfs.detection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInformationRepository extends JpaRepository<marketshelfs.detection.model.ProductInformation,Long> {
}
