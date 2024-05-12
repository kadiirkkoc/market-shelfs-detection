package marketshelfs.detection.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
public class ShelfDto {

    private String category;
    private String outlierProductCategory;
    private Integer productCount;
    private Integer outlierProductCount;
    private Integer totalProductCount;
    private Float relationshipRatio;
    private MultipartFile file;
    private String uploadedImageUrl;
    private String renderedImageUrl;
    private String username;
}
