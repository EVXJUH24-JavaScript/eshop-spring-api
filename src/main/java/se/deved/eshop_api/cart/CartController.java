package se.deved.eshop_api.cart;

import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.deved.eshop_api.product.ProductController;
import se.deved.eshop_api.product.ProductRepository;
import se.deved.eshop_api.user.User;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin
public class CartController {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @PostMapping("/add-product")
    public ResponseEntity<String> addProductToCart(
            @AuthenticationPrincipal User user, @RequestParam UUID productId
    ) {
        var product = productRepository.findById(productId).orElseThrow(() -> new NullPointerException("Product not found"));
        var cart = user.getCart();

        var existingCartItem = cart.getCartItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst();
        if (existingCartItem.isPresent()) {
            var cartItem = existingCartItem.get();
            cartItem.setAmount(cartItem.getAmount() + 1);

            cartItemRepository.save(cartItem);
            return ResponseEntity.ok("Added amount");
        }

        var cartItem = new CartItem(1, cart, product);
        cart.getCartItems().add(cartItem);

        cartItemRepository.save(cartItem);
        cartRepository.save(cart);

        return ResponseEntity.ok("Created item");
    }

    @DeleteMapping("delete/{productId}")
    public ResponseEntity<String> deleteCartItem(
            @AuthenticationPrincipal User user,
            @PathVariable UUID productId) {
        var cartItem = user.getCart().getCartItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst();
        if (cartItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        cartItemRepository.delete(cartItem.get());
        return ResponseEntity.ok().build();
    }

    @PutMapping("modify/{productId}")
    public ResponseEntity<String> deleteCartItem(
            @AuthenticationPrincipal User user,
            @PathVariable UUID productId,
            @RequestParam int amount
    ) {
        var optionalCartItem = user.getCart().getCartItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst();
        if (optionalCartItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var cartItem = optionalCartItem.get();
        cartItem.setAmount(cartItem.getAmount() + amount);
        if (cartItem.getAmount() <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItemRepository.save(cartItem);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<CartItemResponse> getCart(@AuthenticationPrincipal User user) {
        var cart = user.getCart();
        return cart.getCartItems()
                .stream()
                .map(model -> new CartItemResponse(model.getId(), model.getAmount(), ProductController.ProductResponse.fromEntity(model.getProduct())))
                .toList();
    }

    // Add
    // Remove
    // Inc
    // Dec
    // Get

    @AllArgsConstructor
    @Getter
    public static class CartItemResponse {
        private UUID id;
        private int amount;
        private ProductController.ProductResponse product;
    }
}
