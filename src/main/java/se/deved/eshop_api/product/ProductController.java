package se.deved.eshop_api.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@CrossOrigin
public class ProductController {

    private final ProductRepository productRepository;

    @PostMapping("/seed")
    public void seedProducts() {
        var thumbnail = "https://firebasestorage.googleapis.com/v0/b/salinaka-ecommerce.appspot.com/o/products%2F7l3FMZqY8JdfssalDgx2?alt=media&token=be15689c-e12c-4829-9d78-32395ef1e3f7";
        var images = new String[]{
                "https://firebasestorage.googleapis.com/v0/b/salinaka-ecommerce.appspot.com/o/products%2F7l3FMZqY8JdfssalDgx2?alt=media&token=be15689c-e12c-4829-9d78-32395ef1e3f7",
                "https://firebasestorage.googleapis.com/v0/b/salinaka-ecommerce.appspot.com/o/products%2F7l3FMZqY8JdfssalDgx2?alt=media&token=be15689c-e12c-4829-9d78-32395ef1e3f7"
        };

        var products = new Product[]{
                new Product("Pannkakor", "Yummy", "mat", thumbnail, images),
                new Product("Glass", "Väldigt god glass!", "mat", thumbnail, images),
                new Product("Godis", "Väldigt gott godis!", "mat", thumbnail, images),
        };

        productRepository.saveAll(Arrays.stream(products).toList());
    }

    @GetMapping("/all")
    public List<ProductResponse> getAllProducts() {
        return productRepository
                .findAll()
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getAllProducts(@PathVariable UUID id) {
        var product = productRepository.findById(id);
        if (product.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(product.map(ProductResponse::fromEntity).get());
    }

    @AllArgsConstructor
    @Getter
    public static class ProductResponse {
        private UUID id;
        private String title;
        private String description;
        private String thumbnailUrl;
        private String category;
        private String[] images;

        public static ProductResponse fromEntity(Product model) {
            return new ProductResponse(model.getId(), model.getTitle(), model.getDescription(), model.getThumbnailUrl(), model.getCategory(), model.getImages());
        }
    }
}
