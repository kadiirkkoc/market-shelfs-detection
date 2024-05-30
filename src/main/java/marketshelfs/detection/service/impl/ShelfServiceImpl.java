package marketshelfs.detection.service.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import lombok.extern.slf4j.Slf4j;
import marketshelfs.detection.dtos.ShelfDto;
import marketshelfs.detection.enums.UserRole;
import marketshelfs.detection.loggers.MainLogger;
import marketshelfs.detection.model.Shelf;
import marketshelfs.detection.model.User;
import marketshelfs.detection.repository.CorporateUserRepository;
import marketshelfs.detection.repository.IndividualUserRepository;
import marketshelfs.detection.repository.ShelfRepository;
import marketshelfs.detection.repository.UserRepository;
import marketshelfs.detection.security.JwtService;
import marketshelfs.detection.service.ShelfService;
import marketshelfs.detection.utils.FirebaseImageService;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static java.rmi.server.LogStream.log;

@Service
@Slf4j
public class ShelfServiceImpl implements ShelfService {

    private final MainLogger logger = new MainLogger(ShelfServiceImpl.class);
    private final ShelfRepository shelfRepository;
    private final FirebaseImageService firebaseImageService;
    private final UserRepository userRepository;
    private final IndividualUserRepository individualUserRepository;
    private final CorporateUserRepository corporateUserRepository;
    private final JwtService jwtService;

    @Value("${ai-service.process-url}")
    private String aiServiceProcessUrl;

    public ShelfServiceImpl(ShelfRepository shelfRepository, FirebaseImageService firebaseImageService, UserRepository userRepository, IndividualUserRepository individualUserRepository, CorporateUserRepository corporateUserRepository, JwtService jwtService) {
        this.shelfRepository = shelfRepository;
        this.firebaseImageService = firebaseImageService;
        this.userRepository = userRepository;
        this.individualUserRepository = individualUserRepository;
        this.corporateUserRepository = corporateUserRepository;
        this.jwtService = jwtService;
    }

    @Override
    public Map<String, Object> addShelf(ShelfDto shelfDto) {
        // Retrieve user information from the repository based on the username
        Optional<User> user = userRepository.findByUsername(shelfDto.getUsername());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        LocalDateTime now = LocalDateTime.now();
        // Reset daily limit if the last upload was more than 24 hours ago
        if (user.get().getLastUploadTime() == null || Duration.between(user.get().getLastUploadTime(), now).toHours() >= 24) {
            user.get().setDailyLimit(user.get().getUserRole() == UserRole.INDIVIDUAL ? 3 : 10);
        }

        // Check if the user has remaining daily uploads
        if (user.get().getDailyLimit() <= 0) {
            throw new IllegalStateException("Upload limit exceeded");
        }

        // Update the user's last upload time and decrement the daily limit
        user.get().setLastUploadTime(now);
        user.get().setDailyLimit(user.get().getDailyLimit() - 1);
        userRepository.save(user.get());

        // Upload the image to Firebase and get the URL
        String firebaseUploadedUrl = firebaseImageService.upload(shelfDto.getFile());

        // Process the image with the AI service and get enriched shelf data
        ShelfDto enrichedShelfDto = processImageWithAiService();

        // Create a new Shelf object with the enriched data
        Shelf shelf = Shelf.builder()
                .category(enrichedShelfDto.getCategory())
                .outlierProductCategory(enrichedShelfDto.getOutlierProductCategory())
                .productCount(enrichedShelfDto.getProductCount())
                .outlierProductCount(enrichedShelfDto.getOutlierProductCount())
                .totalProductCount(enrichedShelfDto.getTotalProductCount())
                .relationshipRatio(enrichedShelfDto.getRelationshipRatio())
                .uploadedImageUrl(firebaseUploadedUrl)
                .renderedImageUrl(enrichedShelfDto.getRenderedImageUrl())
                .build();

        // Save the new Shelf object to the repository
        shelfRepository.save(shelf);

        // Prepare the response data
        Map<String, Object> response = new HashMap<>();
        response.put("renderedImageUrl", shelf.getRenderedImageUrl());
        response.put("category", shelf.getCategory());
        response.put("outlierProductCategory", shelf.getOutlierProductCategory());
        response.put("productCount", shelf.getProductCount());
        response.put("outlierProductCount", shelf.getOutlierProductCount());
        response.put("totalProductCount", shelf.getTotalProductCount());
        response.put("relationshipRatio", shelf.getRelationshipRatio());
        response.put("dailyLimit", user.get().getDailyLimit());

        return response;
    }

    public ShelfDto processImageWithAiService() {
        // Create an HTTP POST request to the AI service
        HttpPost httpPost = new HttpPost(aiServiceProcessUrl);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Accept", "application/json");

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Execute the request and get the response
            HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());

            // Parse the response JSON
            JsonReader reader = new JsonReader(new StringReader(responseString));
            reader.setLenient(true);
            JsonObject jsonResponse = JsonParser.parseReader(reader).getAsJsonObject();

            // Create and return a ShelfDto object with the parsed data
            return ShelfDto.builder()
                    .category(jsonResponse.has("category") ? jsonResponse.get("category").getAsString() : null)
                    .outlierProductCategory(jsonResponse.has("outlier_product_category") ? String.valueOf(jsonResponse.get("outlier_product_category").getAsString()) : null)
                    .productCount(jsonResponse.has("product_count") ? jsonResponse.get("product_count").getAsInt() : 0)
                    .outlierProductCount(jsonResponse.has("outlier_product_count") ? jsonResponse.get("outlier_product_count").getAsInt() : 0)
                    .totalProductCount(jsonResponse.has("total_product_count") ? jsonResponse.get("total_product_count").getAsInt() : 0)
                    .relationshipRatio(jsonResponse.has("relationship_ratio") ? jsonResponse.get("relationship_ratio").getAsFloat() : 0.0f)
                    .renderedImageUrl(jsonResponse.has("rendered_image_url") ? jsonResponse.get("rendered_image_url").getAsString() : null)
                    .build();

        } catch (IOException e) {
            log("Failed to communicate with AI service" + e);
            throw new RuntimeException("Failed to communicate with AI service", e);
        }
    }
}
