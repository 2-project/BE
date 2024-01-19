package com.github.backendpart.service;


import com.github.backendpart.web.dto.cart.CartDto;
import com.github.backendpart.web.entity.CartEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);


    @Mapping(target = "cartId", source="cartCid")
    @Mapping(target = "productname", source = "product.productName")
    @Mapping(target = "size", source = "option.optionName")
    @Mapping(target = "quantity", source = "cartQuantity")
    @Mapping(target = "price", source = "product.productPrice")
    CartDto CartEntityToDTO(CartEntity cartEntity);
}


