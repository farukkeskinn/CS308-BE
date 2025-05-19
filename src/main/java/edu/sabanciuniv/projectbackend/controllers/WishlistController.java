package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.dto.AddItemRequest;
import edu.sabanciuniv.projectbackend.models.Customer;
import edu.sabanciuniv.projectbackend.models.Wishlist;
import edu.sabanciuniv.projectbackend.repositories.CustomerRepository;
import edu.sabanciuniv.projectbackend.services.WishlistService;
import edu.sabanciuniv.projectbackend.utils.JWTUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService   wishlistService;
    private final CustomerRepository customerRepository;

    public WishlistController(WishlistService wishlistService,
                              CustomerRepository customerRepository) {
        this.wishlistService   = wishlistService;
        this.customerRepository = customerRepository;
    }

    /* ------------------------------------------------------------ */
    /*  helper – JWT → customerId                                   */
    /* ------------------------------------------------------------ */
    private String customerIdFromHeader(String header) {
        if (header == null || !header.startsWith("Bearer "))
            throw new RuntimeException("Missing / invalid Authorization header");

        String email = JWTUtil.getEmailFromToken(header.substring(7));
        return customerRepository.findOptionalByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"))
                .getCustomerId();
    }



    /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */



    /* ------------------------------------------------------------
    GET /api/wishlist/by-customer/{customerId}
    ------------------------------------------------------------ */
    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<?> getWishlistByCustomer(@PathVariable String customerId) {

        Customer customer = wishlistService.getCustomerById(customerId);
        if (customer == null) {
            return ResponseEntity
                    .status(404)
                    .body(Map.of("message", "Customer not found"));
        }

        Wishlist wishlist = wishlistService.getWishlistByCustomer(customer);
        if (wishlist == null) {
            return ResponseEntity
                    .ok(Map.of("message", "No wishlist found for this customer"));
        }

        return ResponseEntity.ok(wishlist);
    }





    @PostMapping("/add-item-to-wishlist")
    public ResponseEntity<?> addItemToWishlist(@RequestBody AddItemRequest request) {
        Wishlist wishlist = wishlistService.addProductToWishlist(request);

        if (wishlist == null) {
            return ResponseEntity.ok(Map.of("message", "There is no enough stock"));
        }

        return ResponseEntity.ok(wishlist);
    }


    @DeleteMapping("/remove-item/{itemId}")
    public void removeItemFromWishlist(@PathVariable String itemId) {
        wishlistService.removeProductFromWishlist(itemId);
    }



        /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */



    /* ------------------------------------------------------------ */
    /*  GET /api/wishlist/{productId}/exists                        */
    /* ------------------------------------------------------------ */
    @GetMapping("/{productId}/exists")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Boolean> existsInWishlist(
            @PathVariable String productId,
            @RequestHeader("Authorization") String auth) {

        boolean flag = wishlistService.isInWishlist(customerIdFromHeader(auth), productId);
        return ResponseEntity.ok(flag);
    }

    /* ------------------------------------------------------------ */
    /*  ADMIN – tüm wishlist’ler                                    */
    /* ------------------------------------------------------------ */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Wishlist> getAllWishlists() {
        return wishlistService.getAllWishlists();
    }
}
