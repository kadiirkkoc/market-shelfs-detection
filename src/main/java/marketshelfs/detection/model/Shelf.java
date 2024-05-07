package marketshelfs.detection.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shelves")
public class Shelf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true)
    private String uuid;

    @Column(name = "category")
    private String category;

    @Column(name = "outlier_product_category")
    private String outlierProductCategory;

    @Column(name = "product_count")
    private int productCount;

    @Column(name = "outlier_product_count")
    private int outlierProductCount;

    @Column(name = "total_product_count")
    private int totalProductCount;

    @Column(name = "relationship_ratio")
    private float relationshipRatio;

    @Column(name = "uploaded_image_url")
    private String uploadedImageUrl;

    @Column(name = "rendered_image_url")
    private String renderedImageUrl;

}
