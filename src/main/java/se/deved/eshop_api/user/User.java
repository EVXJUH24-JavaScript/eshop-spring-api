package se.deved.eshop_api.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import se.deved.eshop_api.cart.Cart;

import java.util.UUID;

@Entity(name = "eshop_users")
@Getter
@Setter
public class User {

    @Id
    private UUID id;

    private String username;
    private String password;

    @OneToOne(mappedBy = "user")
    private Cart cart;

    public User(String username, String password, Cart cart) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.cart = cart;
    }

    public User() {}
}
