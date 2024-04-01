package com.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.backend.model.ProductEntity;
import com.backend.model.UserEntity;
import com.backend.model.WishListEntity;
import com.backend.repository.WishListRepository;
import com.backend.service.WishListService;

@Service
public class WishListServiceImpl implements WishListService {

    @Autowired
    private WishListRepository wishListRepository;

    @Override
    public void addProductToWishList(UserEntity user, ProductEntity product) {
        WishListEntity wish = new WishListEntity();
        wish.setUser(user);
        wish.setProduct(product);
        wishListRepository.save(wish);
    }

    @Override
    public void removeProductFromWishList(UserEntity user, ProductEntity product) {
        WishListEntity wishListEntity = wishListRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new RuntimeException("Wishlist entry not found for removal"));
        wishListRepository.delete(wishListEntity);
    }

    @Override
    public boolean isProductInWishList(UserEntity user, ProductEntity product) {
        return wishListRepository.existsByUserAndProduct(user, product);
    }
}
