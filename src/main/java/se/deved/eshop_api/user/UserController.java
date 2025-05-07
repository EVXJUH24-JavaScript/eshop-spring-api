package se.deved.eshop_api.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.deved.eshop_api.cart.Cart;
import se.deved.eshop_api.cart.CartRepository;
import se.deved.eshop_api.security.JwtService;
import se.deved.eshop_api.user.dto.LoginUserRequest;
import se.deved.eshop_api.user.dto.RegisterUserRequest;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
// Bör endast finnas under utveckling
@CrossOrigin
public class UserController {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final JwtService jwtService;

    @PostMapping("/register")
    public void registerUser(@RequestBody RegisterUserRequest request) {
        var cart = new Cart(null);
        var user = new User(request.getUsername(), request.getPassword(), cart);
        cart.setUser(user);

        userRepository.save(user);
        cartRepository.save(cart);
    }

    @PostMapping("/login")
    public ResponseEntity<AccessToken> login(@RequestBody LoginUserRequest request) {
        var potentialUser = userRepository
                .findByUsername(request.getUsername());
        if (potentialUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = potentialUser.get();

        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var token = jwtService.generateToken(user.getId());
        return ResponseEntity.ok(new AccessToken(token));
    }

    @PostMapping("/test")
    public String test(@AuthenticationPrincipal User user) {
        return "Hello! " + user.getUsername();
    }

    @AllArgsConstructor
    @Getter
    public static class AccessToken {
        private String accessToken;
    }
}
