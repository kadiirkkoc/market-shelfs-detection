package marketshelfs.detection.dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ShelfDto {

    private String category;
    private String outlierProductCategory;
    private int productCount;
    private int outlierProductCount;
    private int totalProductCount;
    private float relationshipRatio;
    private String uploadedImageUrl;
    private String renderedImageUrl;
}
