package se.deved.eshop_api.cart;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import se.deved.eshop_api.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Cart {

    @Id
    private UUID id;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems;

    @OneToOne
    private User user;

    public Cart(User user) {
        this.id = UUID.randomUUID();
        this.cartItems = new ArrayList<>();
        this.user = user;
    }

    public Cart() {}
}
