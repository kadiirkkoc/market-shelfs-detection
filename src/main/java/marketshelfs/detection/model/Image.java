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
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uploaded_image_url")
    private String uploadedImageUrl;

    @Column(name = "rendered_image_url")
    private String renderedImageUrl;

    @ManyToOne
    @JoinColumn(name = "shelf_id")
    private Shelf shelf;
}
