package se.deved.eshop_api.product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import se.deved.eshop_api.cart.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Product {

    @Id
    private UUID id;

    private String title;
    private String description;
    private String thumbnailUrl;
    private String category;

    private String[] images;

    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems;

    public Product(String title, String description, String category, String thumbnailUrl, String[] images) {
       this.id = UUID.randomUUID();
       this.title = title;
       this.description = description;
       this.thumbnailUrl = thumbnailUrl;
       this.category = category;
       this.images = images;
       this.cartItems = new ArrayList<>();
    }

    public Product() {}
}
