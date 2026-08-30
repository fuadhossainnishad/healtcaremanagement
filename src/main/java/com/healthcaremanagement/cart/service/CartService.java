package com.healthcaremanagement.cart.service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final RedisCacheService redisCacheService;
    private final CartRepository cartRepository;

    public Cart getCart(String userId) {
        // Try Redis first, fallback to DB
        Cart cached = redisCacheService.get("cart:" + userId, Cart.class);
        if (cached != null) return cached;
        Cart dbCart = cartRepository.findByUserId(userId).orElse(new Cart());
        redisCacheService.save("cart:" + userId, dbCart, Duration.ofHours(1));
        return dbCart;
    }

    public void addItem(String userId, String testId, int quantity) {
        // update cart, persist to DB, then update Redis
        // also publish event to RabbitMQ for potential price updates, etc.
    }
}