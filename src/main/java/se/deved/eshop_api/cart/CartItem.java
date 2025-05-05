package se.deved.eshop_api.cart;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import se.deved.eshop_api.product.Product;

import java.util.UUID;

@Entity
@Getter
@Setter
public class CartItem {

    @Id
    private UUID id;

    private int amount;

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Product product;

    public CartItem(int amount, Cart cart, Product product) {
        this.id = UUID.randomUUID();
        this.amount = amount;
        this.cart = cart;
        this.product = product;
    }

    public CartItem() {
    }
}
