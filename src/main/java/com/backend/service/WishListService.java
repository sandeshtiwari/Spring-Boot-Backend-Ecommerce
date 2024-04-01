package com.backend.service;

import com.backend.model.ProductEntity;
import com.backend.model.UserEntity;

public interface WishListService {
    void addProductToWishList(UserEntity user, ProductEntity product);

    void removeProductFromWishList(UserEntity user, ProductEntity product);

    boolean isProductInWishList(UserEntity user, ProductEntity product);

}
