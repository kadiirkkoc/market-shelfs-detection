package marketshelfs.detection.service.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import marketshelfs.detection.dtos.ShelfDto;
import marketshelfs.detection.loggers.MainLogger;
import marketshelfs.detection.loggers.messages.ShelfMessage;
import marketshelfs.detection.model.Shelf;
import marketshelfs.detection.repository.ShelfRepository;
import marketshelfs.detection.service.ShelfService;
import marketshelfs.detection.utils.FirebaseImageService;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Service
public class ShelfServiceImpl implements ShelfService {

    private final ShelfRepository shelfRepository;
    private final MainLogger logger = new MainLogger(ShelfServiceImpl.class);
    private final FirebaseImageService firebaseImageService;

    @Value("${ai-service.process-url}")
    private String aiServiceProcessUrl;

    public ShelfServiceImpl(ShelfRepository shelfRepository, FirebaseImageService firebaseImageService) {
        this.shelfRepository = shelfRepository;
        this.firebaseImageService = firebaseImageService;
    }

    @Override
    public String addShelf(ShelfDto shelfDto) {
        String firebaseUploadedUrl = firebaseImageService.upload(shelfDto.getFile());

        ShelfDto enrichedShelfDto = processImageWithAiService();

        Shelf shelf = Shelf.builder()
                .category(enrichedShelfDto.getCategory())
                .outlierProductCategory(enrichedShelfDto.getOutlierProductCategory())
                .productCount(enrichedShelfDto.getProductCount())
                .outlierProductCount(enrichedShelfDto.getOutlierProductCount())
                .totalProductCount(enrichedShelfDto.getTotalProductCount())
                .relationshipRatio(enrichedShelfDto.getRelationshipRatio())
                .uploadedImageUrl(enrichedShelfDto.getUploadedImageUrl())
                .renderedImageUrl(enrichedShelfDto.getRenderedImageUrl())
                .build();

        shelfRepository.save(shelf);

        return ShelfMessage.CREATE + shelf.getId();
    }

    public ShelfDto processImageWithAiService() {
        HttpPost httpPost = new HttpPost("aiServiceProcessUrl");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Accept", "application/json");

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());
            JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();

                return ShelfDto.builder()
                    .category(jsonResponse.has("category") ? jsonResponse.get("category").getAsString() : null)
                    .outlierProductCategory(jsonResponse.has("outlier_product_category") ? jsonResponse.get("outlier_product_category").getAsString() : null)
                    .productCount(jsonResponse.has("product_count") ? jsonResponse.get("product_count").getAsInt() : 0)
                    .outlierProductCount(jsonResponse.has("outlier_product_count") ? jsonResponse.get("outlier_product_count").getAsInt() : 0)
                    .totalProductCount(jsonResponse.has("total_product_count") ? jsonResponse.get("total_product_count").getAsInt() : 0)
                    .relationshipRatio(jsonResponse.has("relationship_ratio") ? jsonResponse.get("relationship_ratio").getAsFloat() : 0.0f)
                    .renderedImageUrl(jsonResponse.has("rendered_image") ? jsonResponse.get("rendered_image").getAsString() : null)
                    .build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

