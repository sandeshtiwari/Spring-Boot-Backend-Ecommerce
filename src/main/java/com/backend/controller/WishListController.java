package com.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.dto.ProductDto;
import com.backend.dto.WishListDto;
import com.backend.exceptions.UserNotFoundException;
import com.backend.model.ProductEntity;
import com.backend.model.UserEntity;
import com.backend.service.ProductService; // Assuming exists
import com.backend.service.UserService; // Assuming exists
import com.backend.service.WishListService;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    @Autowired
    private WishListService wishListService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public String addProductToWishList(@RequestParam("username") String username,
            @RequestParam("productId") int productId) {
        UserEntity user = userService.getByUsername(username);
        ProductEntity product = productService.getProductById(productId);
        wishListService.addProductToWishList(user, product);
        return "Product added to wishlist successfully";
    }

    @DeleteMapping("/remove")
    public String removeProductFromWishList(@RequestParam("username") String username,
            @RequestParam("productId") int productId) {
        UserEntity user = userService.getByUsername(username);
        ProductEntity product = productService.getProductById(productId);
        wishListService.removeProductFromWishList(user, product);
        return "Product removed from wishlist successfully";
    }

    @PostMapping("/exists")
    public boolean isProductInWishList(@RequestParam("username") String username,
            @RequestParam("productId") int productId) {
        UserEntity user = userService.getByUsername(username);
        ProductEntity product = productService.getProductById(productId);
        return wishListService.isProductInWishList(user, product);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<ProductDto>> getWishlistByUsername(@PathVariable String username) {
        try {
            WishListDto wishListDto = wishListService.getWishListByUsername(username);
            return ResponseEntity.ok(wishListDto.getProducts());
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
