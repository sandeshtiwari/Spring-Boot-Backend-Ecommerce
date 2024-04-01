package com.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.dto.ProductDto;
import com.backend.dto.WishListDto;
import com.backend.exceptions.UserNotFoundException;
import com.backend.model.ProductEntity;
import com.backend.model.UserEntity;
import com.backend.model.WishListEntity;
import com.backend.repository.WishListRepository;
import com.backend.service.WishListService;

@Service
public class WishListServiceImpl implements WishListService {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

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

    @Override
    public WishListDto getWishListByUsername(String username) {
        UserEntity userEntity = userServiceImpl.getByUsername(username);

        List<WishListEntity> wishListEntities = wishListRepository.findByUser(userEntity);

        List<ProductDto> productDtos = wishListEntities.stream()
                .map(wishListEntity -> mapToProductDto(wishListEntity.getProduct()))
                .collect(Collectors.toList());

        return new WishListDto(productDtos);

    }

    private ProductDto mapToProductDto(ProductEntity productEntity) {
        ProductDto productDto = new ProductDto();
        productDto.setProductId(productEntity.getProductId());
        productDto.setName(productEntity.getName());
        productDto.setDescription(productEntity.getDescription());
        productDto.setPrice(productEntity.getPrice());
        productDto.setImage(productEntity.getImage());
        productDto.setCountInStock(productEntity.getCountInStock());
        return productDto;
    }
}
